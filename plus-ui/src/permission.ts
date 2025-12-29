import { to as tos } from 'await-to-js';
import router from './router';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { getToken } from '@/utils/auth';
import { isHttp, isPathMatch } from '@/utils/validate';
import { isRelogin } from '@/utils/request';
import { useUserStore } from '@/store/modules/user';
import { useSettingsStore } from '@/store/modules/settings';
import { usePermissionStore } from '@/store/modules/permission';
import { ElMessage } from 'element-plus/es';

NProgress.configure({ showSpinner: false });
const whiteList = ['/login', '/register', '/social-callback', '/register*', '/register/*'];

// 需要隐藏首页的租户ID列表（演示租户）- 登录后跳转到数据大屏
const HIDE_HOME_TENANT_IDS = ['461397'];

const isWhiteList = (path: string) => {
  return whiteList.some((pattern) => isPathMatch(pattern, path));
};

router.beforeEach(async (to, from, next) => {
  NProgress.start();
  if (getToken()) {
    to.meta.title && useSettingsStore().setTitle(to.meta.title as string);
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' });
      NProgress.done();
    } else if (isWhiteList(to.path)) {
      next();
    } else {
      if (useUserStore().roles.length === 0) {
        isRelogin.show = true;
        // 判断当前用户是否已拉取完user_info信息
        const [err] = await tos(useUserStore().getInfo());
        if (err) {
          await useUserStore().logout();
          ElMessage.error(err);
          next({ path: '/' });
        } else {
          isRelogin.show = false;
          const accessRoutes = await usePermissionStore().generateRoutes();
          // 根据roles权限生成可访问的路由表
          accessRoutes.forEach((route) => {
            if (!isHttp(route.path)) {
              router.addRoute(route); // 动态添加可访问路由表
            }
          });
          
          // 演示租户登录后跳转到数据大屏而不是首页
          let targetPath = to.path;
          if (to.path === '/' || to.path === '/index') {
            const currentTenantId = useUserStore().tenantId;
            if (HIDE_HOME_TENANT_IDS.includes(currentTenantId)) {
              targetPath = '/health/dashboard';
            }
          }
          
          // @ts-expect-error hack方法 确保addRoutes已完成
          next({ path: targetPath, replace: true, params: to.params, query: to.query, hash: to.hash, name: to.name as string }); // hack方法 确保addRoutes已完成
        }
      } else {
        // 已登录用户访问首页时，演示租户重定向到数据大屏
        if (to.path === '/' || to.path === '/index') {
          const currentTenantId = useUserStore().tenantId;
          if (HIDE_HOME_TENANT_IDS.includes(currentTenantId)) {
            next({ path: '/health/dashboard', replace: true });
            return;
          }
        }
        next();
      }
    }
  } else {
    // 没有token
    if (isWhiteList(to.path)) {
      // 在免登录白名单，直接进入
      next();
    } else {
      const redirect = encodeURIComponent(to.fullPath || '/');
      next(`/login?redirect=${redirect}`); // 否则全部重定向到登录页
      NProgress.done();
    }
  }
});

router.afterEach(() => {
  NProgress.done();
});
