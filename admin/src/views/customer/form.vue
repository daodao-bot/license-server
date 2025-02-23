<script setup lang="ts">
import { ref } from "vue";
import ReCol from "@/components/ReCol";
import { usePublicHooks } from "@/views/system/hooks";
import { reactive } from "vue";
import type { FormRules } from "element-plus";
import { CustomerUpsert } from "@/api/customer";

interface FormItemProps extends CustomerUpsert {
  /** 用于判断是`新增`还是`修改` */
  title: string;
}
interface FormProps {
  formInline: FormItemProps;
}

/** 自定义表单规则校验 */
const formRules = reactive<FormRules>({
  name: [{ required: true, message: "名称：为必填项", trigger: "blur" }],
  phone: [{ required: true, message: "手机：为必填项", trigger: "blur" }],
  email: [{ required: true, message: "邮箱：为必填项", trigger: "blur" }]
});

const props = withDefaults(defineProps<FormProps>(), {
  formInline: () => ({
    title: "新增",
    name: null,
    phone: null,
    email: null,
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
        <el-form-item label="名称" prop="name">
          <el-input
            v-model="newFormInline.name"
            clearable
            placeholder="请输入..."
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="手机" prop="phone">
          <el-input
            v-model="newFormInline.phone"
            clearable
            placeholder="请输入..."
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model="newFormInline.email"
            clearable
            placeholder="请输入..."
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
