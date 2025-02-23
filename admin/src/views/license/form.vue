<script setup lang="ts">
import { ref } from "vue";
import ReCol from "@/components/ReCol";
import { usePublicHooks } from "@/views/system/hooks";
import { reactive } from "vue";
import type { FormRules } from "element-plus";
import { LicenseUpsert } from "@/api/license";

interface FormItemProps extends LicenseUpsert {
  /** 用于判断是`新增`还是`修改` */
  title: string;
}
interface FormProps {
  formInline: FormItemProps;
}

/** 自定义表单规则校验 */
const formRules = reactive<FormRules>({
  productId: [{ required: true, message: "产品：为必填项", trigger: "blur" }],
  customerId: [{ required: true, message: "客户：为必填项", trigger: "blur" }]
});

const props = withDefaults(defineProps<FormProps>(), {
  formInline: () => ({
    title: "新增",
    productId: null,
    customerId: null,
    valid: true
  })
});

const ruleFormRef = ref();
const { switchStyle } = usePublicHooks();
const newFormInline = ref(props.formInline);

function getRef() {
  return ruleFormRef.value;
}

defineExpose({ getRef });
</script>

<template>
  <el-form
    ref="ruleFormRef"
    :model="newFormInline"
    :rules="formRules"
    label-width="82px"
  >
    <el-row :gutter="30">
      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="产品 ID" prop="productId">
          <el-input
            v-model="newFormInline.productId"
            clearable
            placeholder="请输入..."
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="客户 ID" prop="customerId">
          <el-input
            v-model="newFormInline.customerId"
            clearable
            placeholder="请输入..."
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="应用 ID" prop="appId">
          <el-input
            v-model="newFormInline.appId"
            clearable
            placeholder="请输入..."
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="许可证号" prop="license">
          <el-input
            v-model="newFormInline.license"
            clearable
            placeholder="请输入..."
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="有效期开始" prop="periodStart">
          <el-input
            v-model="newFormInline.periodStart"
            clearable
            placeholder="请输入..."
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="有效期结束" prop="periodEnd">
          <el-input
            v-model="newFormInline.periodEnd"
            clearable
            placeholder="请输入..."
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="是否长期">
          <el-switch
            v-model="newFormInline.longTerm"
            inline-prompt
            :active-value="true"
            :inactive-value="false"
            active-text="长期"
            inactive-text="短期"
            :style="switchStyle"
          />
        </el-form-item>
      </re-col>

      <re-col
        v-if="newFormInline.title === '新增'"
        :value="12"
        :xs="24"
        :sm="24"
      >
        <el-form-item label="数据状态">
          <el-switch
            v-model="newFormInline.valid"
            inline-prompt
            :active-value="true"
            :inactive-value="false"
            active-text="启用"
            inactive-text="停用"
            :style="switchStyle"
          />
        </el-form-item>
      </re-col>
    </el-row>
  </el-form>
</template>
