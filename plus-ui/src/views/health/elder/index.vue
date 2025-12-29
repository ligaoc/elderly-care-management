<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="用户编码" prop="userCode">
              <el-input v-model="queryParams.userCode" placeholder="请输入用户编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="性别" prop="gender">
              <el-select v-model="queryParams.gender" placeholder="性别" clearable>
                <el-option v-for="dict in sys_user_sex" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="所属组织" prop="orgId">
              <el-tree-select
                v-model="queryParams.orgId"
                :data="organizationOptions"
                :props="{ value: 'id', label: 'orgName', children: 'children' } as any"
                value-key="id"
                placeholder="选择所属组织"
                check-strictly
                clearable
              />
            </el-form-item>
            <el-form-item label="护理等级" prop="careLevel">
              <el-select v-model="queryParams.careLevel" placeholder="护理等级" clearable>
                <el-option label="自理" value="1" />
                <el-option label="半自理" value="2" />
                <el-option label="不能自理" value="3" />
                <el-option label="特护" value="4" />
              </el-select>
            </el-form-item>
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="queryParams.riskLevel" placeholder="风险等级" clearable>
                <el-option label="低风险" value="1" />
                <el-option label="中风险" value="2" />
                <el-option label="高风险" value="3" />
              </el-select>
            </el-form-item>
            <el-form-item label="年龄范围">
              <el-input-number v-model="queryParams.ageStart" :min="0" :max="150" placeholder="最小年龄" style="width: 100px;" />
              <span style="margin: 0 8px;">-</span>
              <el-input-number v-model="queryParams.ageEnd" :min="0" :max="150" placeholder="最大年龄" style="width: 100px;" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="hover">
      <template #header>
        <el-row :gutter="10">
          <el-col :span="1.5">
            <el-button v-hasPermi="['health:elder:add']" type="primary" plain icon="Plus" @click="handleAdd()">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['health:elder:edit']" type="success" plain :disabled="single" icon="Edit" @click="handleUpdate()">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['health:elder:remove']" type="danger" plain :disabled="multiple" icon="Delete" @click="handleDelete()">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-dropdown class="mt-[1px]">
              <el-button plain type="info">
                更多
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item icon="Download" @click="importTemplate">下载模板</el-dropdown-item>
                  <el-dropdown-item v-if="checkPermi(['health:elder:import'])" icon="Top" @click="handleImport">导入数据</el-dropdown-item>
                  <el-dropdown-item v-if="checkPermi(['health:elder:export'])" icon="Download" @click="handleExport">导出数据</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-col>
          <right-toolbar v-model:show-search="showSearch" :columns="columns" @query-table="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="elderList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column v-if="columns[0].visible" key="userCode" label="用户编码" align="center" prop="userCode" width="120" />
        <el-table-column v-if="columns[1].visible" key="name" label="姓名" align="center" prop="name" width="100" />
        <el-table-column v-if="columns[2].visible" key="gender" label="性别" align="center" width="80">
          <template #default="scope">
            <dict-tag :options="sys_user_sex" :value="scope.row.gender" />
          </template>
        </el-table-column>
        <el-table-column v-if="columns[3].visible" key="age" label="年龄" align="center" prop="age" width="80" />
        <el-table-column v-if="columns[4].visible" key="phone" label="手机号" align="center" prop="phone" width="120" />
        <el-table-column v-if="columns[5].visible" key="orgName" label="所属组织" align="center" prop="orgName" width="120" :show-overflow-tooltip="true" />
        <el-table-column v-if="columns[6].visible" key="bmi" label="BMI" align="center" width="80">
          <template #default="scope">
            <span v-if="scope.row.bmi">{{ scope.row.bmi }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[7].visible" key="careLevel" label="护理等级" align="center" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.careLevel === '1'" type="success">自理</el-tag>
            <el-tag v-else-if="scope.row.careLevel === '2'" type="primary">半自理</el-tag>
            <el-tag v-else-if="scope.row.careLevel === '3'" type="warning">不能自理</el-tag>
            <el-tag v-else-if="scope.row.careLevel === '4'" type="danger">特护</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[8].visible" key="riskLevel" label="风险等级" align="center" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.riskLevel === '1'" type="success">低风险</el-tag>
            <el-tag v-else-if="scope.row.riskLevel === '2'" type="warning">中风险</el-tag>
            <el-tag v-else-if="scope.row.riskLevel === '3'" type="danger">高风险</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[9].visible" key="address" label="地址" align="center" prop="address" :show-overflow-tooltip="true" />
        <el-table-column v-if="columns[10].visible" label="创建时间" align="center" prop="createTime" width="160">
          <template #default="scope">
            <span>{{ proxy.parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="详情" placement="top">
              <el-button link type="primary" icon="View" @click="handleDetail(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="修改" placement="top">
              <el-button v-hasPermi="['health:elder:edit']" link type="primary" icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button v-hasPermi="['health:elder:remove']" link type="primary" icon="Delete" @click="handleDelete(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="联系人" placement="top">
              <el-button link type="primary" icon="User" @click="handleContact(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        :total="total"
        @pagination="getList"
      />
    </el-card>

    <!-- 添加或修改老人对话框 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title" destroy-on-close append-to-body width="800px">
      <el-form ref="elderFormRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" placeholder="请选择性别">
                <el-option v-for="dict in sys_user_sex" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthday">
              <el-date-picker
                v-model="form.birthday"
                type="date"
                placeholder="选择出生日期"
                value-format="YYYY-MM-DD"
                @change="calculateAge"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入身份证号" maxlength="18" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属组织" prop="orgId">
              <el-tree-select
                v-model="form.orgId"
                :data="organizationOptions"
                :props="{ value: 'id', label: 'orgName', children: 'children' } as any"
                value-key="id"
                placeholder="选择所属组织"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="身高(cm)" prop="height">
              <el-input-number v-model="form.height" :min="50" :max="250" :precision="1" @change="calculateBMI" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="体重(kg)" prop="weight">
              <el-input-number v-model="form.weight" :min="20" :max="200" :precision="1" @change="calculateBMI" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="BMI">
              <el-input v-model="calculatedBMI" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="护理等级" prop="careLevel">
              <el-select v-model="form.careLevel" placeholder="请选择护理等级">
                <el-option label="自理" value="1" />
                <el-option label="半自理" value="2" />
                <el-option label="不能自理" value="3" />
                <el-option label="特护" value="4" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="form.riskLevel" placeholder="请选择风险等级">
                <el-option label="低风险" value="1" />
                <el-option label="中风险" value="2" />
                <el-option label="高风险" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入地址" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="既往病史" prop="medicalHistory">
              <el-input v-model="form.medicalHistory" type="textarea" :rows="3" placeholder="请输入既往病史，多个病史用逗号分隔" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 老人详情对话框 -->
    <el-dialog v-model="detailDialog.visible" title="老人详情" destroy-on-close append-to-body width="800px">
      <el-descriptions :column="3" border>
        <el-descriptions-item label="用户编码">{{ currentElder.userCode }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ currentElder.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          <dict-tag :options="sys_user_sex" :value="currentElder.gender" />
        </el-descriptions-item>
        <el-descriptions-item label="年龄">{{ currentElder.age }}岁</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ currentElder.birthday }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentElder.phone }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ currentElder.idCard }}</el-descriptions-item>
        <el-descriptions-item label="所属组织">{{ currentElder.orgName }}</el-descriptions-item>
        <el-descriptions-item label="BMI">{{ currentElder.bmi || '-' }}</el-descriptions-item>
        <el-descriptions-item label="护理等级">
          <el-tag v-if="currentElder.careLevel === '1'" type="success">自理</el-tag>
          <el-tag v-else-if="currentElder.careLevel === '2'" type="primary">半自理</el-tag>
          <el-tag v-else-if="currentElder.careLevel === '3'" type="warning">不能自理</el-tag>
          <el-tag v-else-if="currentElder.careLevel === '4'" type="danger">特护</el-tag>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag v-if="currentElder.riskLevel === '1'" type="success">低风险</el-tag>
          <el-tag v-else-if="currentElder.riskLevel === '2'" type="warning">中风险</el-tag>
          <el-tag v-else-if="currentElder.riskLevel === '3'" type="danger">高风险</el-tag>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ proxy.parseTime(currentElder.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="3">{{ currentElder.address }}</el-descriptions-item>
        <el-descriptions-item label="既往病史" :span="3">
          <div style="max-height: 100px; overflow-y: auto;">{{ currentElder.medicalHistory || '无' }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">
          <div style="max-height: 100px; overflow-y: auto;">{{ currentElder.remark || '无' }}</div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 紧急联系人管理对话框 -->
    <el-dialog v-model="contactDialog.visible" title="紧急联系人管理" destroy-on-close append-to-body width="900px">
      <div class="mb-4">
        <el-button type="primary" icon="Plus" @click="handleAddContact">新增联系人</el-button>
      </div>
      
      <el-table v-loading="contactLoading" border :data="contactList">
        <el-table-column label="姓名" prop="name" width="100" />
        <el-table-column label="关系" prop="relation" width="100" />
        <el-table-column label="联系电话" prop="phone" width="120" />
        <el-table-column label="主要联系人" align="center" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.isPrimary === '1'" type="success">是</el-tag>
            <el-tag v-else type="info">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="通知方式" prop="notifyType" width="100" />
        <el-table-column label="地址" prop="address" :show-overflow-tooltip="true" />
        <el-table-column label="操作" fixed="right" width="150">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdateContact(scope.row)">修改</el-button>
            <el-button link type="primary" icon="Delete" @click="handleDeleteContact(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 添加或修改联系人对话框 -->
    <el-dialog v-model="contactFormDialog.visible" :title="contactFormDialog.title" destroy-on-close append-to-body width="500px">
      <el-form ref="contactFormRef" :model="contactForm" :rules="contactRules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="contactForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="关系" prop="relation">
          <el-select v-model="contactForm.relation" placeholder="请选择关系">
            <el-option label="子女" value="child" />
            <el-option label="配偶" value="spouse" />
            <el-option label="父母" value="parent" />
            <el-option label="兄弟姐妹" value="sibling" />
            <el-option label="朋友" value="friend" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="contactForm.phone" placeholder="请输入联系电话" maxlength="11" />
        </el-form-item>
        <el-form-item label="主要联系人" prop="isPrimary">
          <el-radio-group v-model="contactForm.isPrimary">
            <el-radio value="1">是</el-radio>
            <el-radio value="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="通知方式" prop="notifyType">
          <el-select v-model="contactForm.notifyType" placeholder="请选择通知方式">
            <el-option label="电话" value="phone" />
            <el-option label="短信" value="sms" />
            <el-option label="微信" value="wechat" />
            <el-option label="邮件" value="email" />
          </el-select>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="contactForm.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="contactForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitContactForm">确 定</el-button>
          <el-button @click="contactFormDialog.visible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 老人导入对话框 -->
    <el-dialog v-model="upload.open" :title="upload.title" width="400px" append-to-body>
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload">
          <i-ep-upload-filled />
        </el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="text-center el-upload__tip">
            <div class="el-upload__tip"><el-checkbox v-model="upload.updateSupport" />是否更新已经存在的老人数据</div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="HealthElder" lang="ts">
import { 
  listElder, 
  getElder, 
  delElder, 
  addElder, 
  updateElder,
  exportElder,
  importElder
} from '@/api/health/elder';
import { 
  HealthElderForm, 
  HealthElderQuery, 
  HealthElderVO 
} from '@/api/health/elder/types';
import { 
  listEmergencyContact,
  addEmergencyContact,
  updateEmergencyContact,
  delEmergencyContact
} from '@/api/health/emergencyContact';
import { 
  HealthEmergencyContactVO,
  HealthEmergencyContactForm 
} from '@/api/health/emergencyContact/types';
import { treeOrganization } from '@/api/health/organization';
import { HealthOrganizationTreeVO } from '@/api/health/organization/types';
import { globalHeaders } from '@/utils/request';
import { checkPermi } from '@/utils/permission';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_user_sex } = toRefs<any>(proxy?.useDict('sys_user_sex'));

const elderList = ref<HealthElderVO[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<number | string>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const organizationOptions = ref<HealthOrganizationTreeVO[]>([]);
const currentElder = ref<HealthElderVO>({} as HealthElderVO);
const calculatedBMI = ref<string>('');

// 联系人相关
const contactList = ref<HealthEmergencyContactVO[]>([]);
const contactLoading = ref(false);

// 列显隐信息
const columns = ref<FieldOption[]>([
  { key: 0, label: `用户编码`, visible: true, children: [] },
  { key: 1, label: `姓名`, visible: true, children: [] },
  { key: 2, label: `性别`, visible: true, children: [] },
  { key: 3, label: `年龄`, visible: true, children: [] },
  { key: 4, label: `手机号`, visible: true, children: [] },
  { key: 5, label: `所属组织`, visible: true, children: [] },
  { key: 6, label: `BMI`, visible: true, children: [] },
  { key: 7, label: `护理等级`, visible: true, children: [] },
  { key: 8, label: `风险等级`, visible: true, children: [] },
  { key: 9, label: `地址`, visible: true, children: [] },
  { key: 10, label: `创建时间`, visible: true, children: [] }
]);

/*** 老人导入参数 */
const upload = reactive<ImportOption>({
  open: false,
  title: '',
  isUploading: false,
  updateSupport: 0,
  headers: globalHeaders(),
  url: import.meta.env.VITE_APP_BASE_API + '/health/elder/import'
});

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const detailDialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const contactDialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const contactFormDialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const queryFormRef = ref<ElFormInstance>();
const elderFormRef = ref<ElFormInstance>();
const contactFormRef = ref<ElFormInstance>();
const uploadRef = ref<ElUploadInstance>();

const initFormData: HealthElderForm = {
  id: undefined,
  name: '',
  gender: '',
  birthday: '',
  phone: '',
  idCard: '',
  orgId: undefined,
  height: undefined,
  weight: undefined,
  medicalHistory: '',
  careLevel: '',
  riskLevel: '',
  address: '',
  status: '0',
  remark: ''
};

const initContactFormData: HealthEmergencyContactForm = {
  id: undefined,
  elderId: undefined,
  name: '',
  relation: '',
  phone: '',
  isPrimary: '0',
  notifyType: '',
  address: '',
  remark: ''
};

const initData: PageData<HealthElderForm, HealthElderQuery> = {
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userCode: undefined,
    name: undefined,
    gender: undefined,
    phone: undefined,
    orgId: undefined,
    careLevel: undefined,
    riskLevel: undefined,
    ageStart: undefined,
    ageEnd: undefined
  },
  rules: {
    name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
    gender: [{ required: true, message: '性别不能为空', trigger: 'change' }],
    birthday: [{ required: true, message: '出生日期不能为空', trigger: 'change' }],
    phone: [{ pattern: /^1[3456789][0-9]\d{8}$/, message: '请输入正确的手机号码', trigger: 'blur' }],
    idCard: [{ pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号码', trigger: 'blur' }]
  }
};

const data = reactive<PageData<HealthElderForm, HealthElderQuery>>(initData);
const { queryParams, form, rules } = toRefs<PageData<HealthElderForm, HealthElderQuery>>(data);

const contactForm = ref<HealthEmergencyContactForm>({ ...initContactFormData });
const contactRules = ref({
  name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
  relation: [{ required: true, message: '关系不能为空', trigger: 'change' }],
  phone: [
    { required: true, message: '联系电话不能为空', trigger: 'blur' },
    { pattern: /^1[3456789][0-9]\d{8}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
});

/** 查询老人列表 */
const getList = async () => {
  loading.value = true;
  const res = await listElder(queryParams.value);
  elderList.value = res.rows;
  total.value = res.total;
  loading.value = false;
};

/** 查询组织树结构 */
const getOrganizationTree = async () => {
  const res = await treeOrganization();
  organizationOptions.value = res.data;
};

/** 计算年龄 */
const calculateAge = () => {
  if (form.value.birthday) {
    const today = new Date();
    const birthDate = new Date(form.value.birthday);
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    // 这里不直接设置age字段，因为后端会自动计算
  }
};

/** 计算BMI */
const calculateBMI = () => {
  if (form.value.height && form.value.weight) {
    const heightInMeters = form.value.height / 100;
    const bmi = form.value.weight / (heightInMeters * heightInMeters);
    calculatedBMI.value = bmi.toFixed(2);
  } else {
    calculatedBMI.value = '';
  }
};

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
};

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  calculatedBMI.value = '';
  elderFormRef.value?.resetFields();
};

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
};

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
};

/** 多选框选中数据 */
const handleSelectionChange = (selection: HealthElderVO[]) => {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
};

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = '添加老人';
};

/** 修改按钮操作 */
const handleUpdate = async (row?: HealthElderVO) => {
  reset();
  const elderId = row?.id || ids.value[0];
  const res = await getElder(elderId);
  form.value = res.data;
  calculateBMI();
  dialog.visible = true;
  dialog.title = '修改老人';
};

/** 详情按钮操作 */
const handleDetail = async (row: HealthElderVO) => {
  const res = await getElder(row.id);
  currentElder.value = res.data;
  detailDialog.visible = true;
};

/** 提交按钮 */
const submitForm = () => {
  elderFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      form.value.id ? await updateElder(form.value) : await addElder(form.value);
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

/** 删除按钮操作 */
const handleDelete = async (row?: HealthElderVO) => {
  const elderIds = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除老人编号为"' + elderIds + '"的数据项？');
  await delElder(elderIds);
  await getList();
  proxy?.$modal.msgSuccess('删除成功');
};

/** 联系人管理 */
const handleContact = async (row: HealthElderVO) => {
  currentElder.value = row;
  await getContactList(row.id);
  contactDialog.visible = true;
};

/** 查询联系人列表 */
const getContactList = async (elderId: string | number) => {
  contactLoading.value = true;
  const res = await listEmergencyContact(elderId);
  contactList.value = res.data;
  contactLoading.value = false;
};

/** 新增联系人 */
const handleAddContact = () => {
  contactForm.value = { ...initContactFormData, elderId: currentElder.value.id };
  contactFormDialog.visible = true;
  contactFormDialog.title = '新增联系人';
};

/** 修改联系人 */
const handleUpdateContact = (row: HealthEmergencyContactVO) => {
  contactForm.value = { ...row };
  contactFormDialog.visible = true;
  contactFormDialog.title = '修改联系人';
};

/** 删除联系人 */
const handleDeleteContact = async (row: HealthEmergencyContactVO) => {
  await proxy?.$modal.confirm('是否确认删除联系人"' + row.name + '"？');
  await delEmergencyContact(row.id);
  await getContactList(currentElder.value.id);
  proxy?.$modal.msgSuccess('删除成功');
};

/** 提交联系人表单 */
const submitContactForm = () => {
  contactFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      contactForm.value.id ? await updateEmergencyContact(contactForm.value) : await addEmergencyContact(contactForm.value);
      proxy?.$modal.msgSuccess('操作成功');
      contactFormDialog.visible = false;
      await getContactList(currentElder.value.id);
    }
  });
};

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download(
    'health/elder/export',
    {
      ...queryParams.value
    },
    `elder_${new Date().getTime()}.xlsx`
  );
};

/** 导入按钮操作 */
const handleImport = () => {
  upload.title = '老人导入';
  upload.open = true;
};

/** 下载模板操作 */
const importTemplate = () => {
  proxy?.download('health/elder/importTemplate', {}, `elder_template_${new Date().getTime()}.xlsx`);
};

/**文件上传中处理 */
const handleFileUploadProgress = () => {
  upload.isUploading = true;
};

/** 文件上传成功处理 */
const handleFileSuccess = (response: any, file: UploadFile) => {
  upload.open = false;
  upload.isUploading = false;
  uploadRef.value?.handleRemove(file);
  ElMessageBox.alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + '</div>', '导入结果', {
    dangerouslyUseHTMLString: true
  });
  getList();
};

/** 提交上传文件 */
function submitFileForm() {
  uploadRef.value?.submit();
}

onMounted(() => {
  getList();
  getOrganizationTree();
});
</script>

<style scoped>
.el-tag {
  margin: 2px;
}
</style>