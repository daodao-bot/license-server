<script setup lang="ts">
import { h, onMounted, reactive, type Ref, ref, toRaw } from "vue";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
import { PureTableBar } from "@/components/RePureTableBar";
import { message } from "@/utils/message";
import { addDialog } from "@/components/ReDialog";
import { type PaginationProps, PureTable } from "@pureadmin/table";

import Delete from "@iconify-icons/ep/delete";
import EditPen from "@iconify-icons/ep/edit-pen";
import Search from "@iconify-icons/ep/search";
import Refresh from "@iconify-icons/ep/refresh";
import AddFill from "@iconify-icons/ri/add-circle-line";

import editForm from "@/views/license/form.vue";
import { useLicense } from "@/views/license/hook";

import { Order, PageParam } from "@/api";
import { LicenseData, LicenseSearch, LicenseUpsert } from "@/api/license";
import { licenseSearch, licenseUpsert } from "@/api/license";
import { doExport } from "@/utils/license/export";
import Download from "@iconify-icons/ep/download";
import { emptyToNull } from "@/utils/license/common";

defineOptions({
  name: "License-Index"
});

const pagination = reactive<PaginationProps>({
  total: 0,
  pageSize: 10,
  currentPage: 1,
  pageSizes: [10, 20, 50, 100],
  background: true
});

const form = reactive<LicenseUpsert>({
  id: null,
  productId: null,
  customerId: null,
  valid: null
});
const formRef = ref();
const tableRef = ref();
const dataList: Ref<LicenseData[]> = ref([]);
const loading = ref(true);
const selectedNum = ref(0);
const order = ref<Order>({ property: "id", direction: "ASC" });

const { columns } = useLicense();

const onSearch = async () => {
  loading.value = true;
  const searchParam: LicenseSearch = toRaw(form);
  const param: PageParam<LicenseSearch> = {
    page: pagination.currentPage,
    size: pagination.pageSize,
    param: {
      ...searchParam
    },
    matches: [
      {
        property: "id",
        mode: "EXACT"
      }
    ],
    orders: [
      {
        property: order.value.property,
        direction: order.value.direction
      }
    ]
  };
  licenseSearch({ param })
    .then(({ data }) => {
      dataList.value = data.list ?? [];
      pagination.total = data.total ?? 0;
      pagination.pageSize = data.size ?? 10;
      pagination.currentPage = data.page ?? 1;
    })
    .catch(error => {
      console.error("error", error);
      loading.value = false;
    });

  setTimeout(() => {
    loading.value = false;
  }, 500);
};

const handleSortChange = (sort: { prop: string; order: string }) => {
  order.value = {
    property: sort.prop,
    direction: sort.order === "ascending" ? "ASC" : "DESC"
  };
  onSearch();
};

const resetForm = (formEl: { resetFields: () => void }) => {
  if (!formEl) return;
  formEl.resetFields();
  onSearch();
};

const onExport = async () => {
  const searchParam: LicenseSearch = toRaw(form);
  let page = 1;
  let size = 1000;
  const pageParam: PageParam<LicenseSearch> = {
    page: page,
    size: size,
    param: {
      ...searchParam
    },
    matches: [
      {
        property: "id",
        mode: "EXACT"
      }
    ],
    orders: [
      {
        property: order.value.property,
        direction: order.value.direction
      }
    ]
  };
  await doExport(licenseSearch, pageParam);
};

const openDialog = (title = "新增", row?: LicenseData) => {
  addDialog({
    title: `${title}数据`,
    props: {
      formInline: {
        title,
        ...row
      }
    },
    width: "46%",
    draggable: true,
    fullscreenIcon: true,
    closeOnClickModal: false,
    contentRenderer: () => h(editForm, { ref: formRef, formInline: null }),
    beforeSure: (done, { options }) => {
      const FormRef = formRef.value.getRef();
      const curData = options.props.formInline as LicenseData;

      function chores() {
        message(`您${title}了 id = ${curData.id} 的这条数据`, {
          type: "success"
        });
        done(); // 关闭弹框
        onSearch(); // 刷新表格数据
      }

      FormRef.validate((valid: any) => {
        if (valid) {
          console.debug("curData", curData);
          // 表单规则校验通过
          if (title === "新增") {
            // 实际开发先调用新增接口，再进行下面操作
            const upsertParam: LicenseUpsert = emptyToNull({
              id: null,
              ...curData
            }) as LicenseUpsert;
            licenseUpsert({ param: upsertParam })
              .then(({ data }) => {
                console.debug("data", data);
                chores();
              })
              .catch(error => {
                console.error("error", error);
              });
          } else {
            // 实际开发先调用编辑接口，再进行下面操作
            const upsertParam: LicenseUpsert = emptyToNull({
              id: curData.id,
              ...curData
            }) as LicenseUpsert;
            licenseUpsert({ param: upsertParam })
              .then(({ data }) => {
                console.debug("data", data);
                chores();
              })
              .catch(error => {
                console.error("error", error);
              });
          }
        }
      });
    }
  });
};

function handleDelete(row: { id: any }) {
  console.debug("row", row);
  message(`暂未支持此项功能！`, { type: "error" });
  onSearch();
}

function handleSizeChange(val: number) {
  console.debug(`${val} items per page`);
  pagination.pageSize = val;
  onSearch();
}

function handleCurrentChange(val: number) {
  console.debug(`current page: ${val}`);
  pagination.currentPage = val;
  onSearch();
}

/** 当CheckBox选择项发生变化时会触发该事件 */
function handleSelectionChange(val: string | any[]) {
  selectedNum.value = val.length;
  // 重置表格高度
  tableRef.value.setAdaptive();
}

/** 取消选择 */
function onSelectionCancel() {
  selectedNum.value = 0;
  // 用于多选表格，清空用户的选择
  tableRef.value.getTableRef().clearSelection();
}

/** 批量删除 */
function onBatchDel() {
  // 返回当前选中的行
  const curSelected = tableRef.value.getTableRef().getSelectionRows();
  console.debug("curSelected", curSelected);
  // 接下来根据实际业务，通过选中行的某项数据，比如下面的id，调用接口进行批量删除
  message(`暂未支持此项功能！`, {
    type: "error"
  });
  tableRef.value.getTableRef().clearSelection();
}

onMounted(async () => {
  await onSearch();
});
</script>

<template>
  <div class="flex justify-between">
    <div class="w-[calc(100%)]">
      <el-form
        ref="formRef"
        :inline="true"
        :model="form"
        class="search-form bg-bg_color w-[99/100] pl-8 pt-[12px]"
      >
        <el-form-item label="产品 ID" prop="productId">
          <el-input
            v-model="form.productId"
            placeholder="请输入..."
            clearable
            class="!w-[160px]"
          />
        </el-form-item>
        <el-form-item label="客户 ID" prop="customerId">
          <el-input
            v-model="form.customerId"
            placeholder="请输入..."
            clearable
            class="!w-[160px]"
          />
        </el-form-item>
        <el-form-item label="应用 ID" prop="appId">
          <el-input
            v-model="form.appId"
            placeholder="请输入..."
            clearable
            class="!w-[160px]"
          />
        </el-form-item>
        <el-form-item label="许可证号" prop="license">
          <el-input
            v-model="form.license"
            placeholder="请输入..."
            clearable
            class="!w-[160px]"
          />
        </el-form-item>
        <el-form-item label="有效期开始" prop="periodStart">
          <el-input
            v-model="form.periodStart"
            placeholder="请输入..."
            clearable
            class="!w-[160px]"
          />
        </el-form-item>
        <el-form-item label="有效期结束" prop="periodEnd">
          <el-input
            v-model="form.periodEnd"
            placeholder="请输入..."
            clearable
            class="!w-[160px]"
          />
        </el-form-item>
        <el-form-item label="是否长期" prop="valid">
          <el-select
            v-model="form.longTerm"
            placeholder="请选择"
            clearable
            class="!w-[160px]"
          >
            <el-option label="长期" :value="true" />
            <el-option label="短期" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="valid">
          <el-select
            v-model="form.valid"
            placeholder="请选择"
            clearable
            class="!w-[160px]"
          >
            <el-option label="启用" :value="true" />
            <el-option label="停用" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            :icon="useRenderIcon(Search)"
            :loading="loading"
            plain
            @click="onSearch"
          >
            搜索
          </el-button>
          <el-button :icon="useRenderIcon(Refresh)" @click="resetForm(formRef)">
            重置
          </el-button>
          <el-button
            type="warning"
            plain
            :icon="useRenderIcon(Download)"
            @click="onExport"
          >
            导出
          </el-button>
        </el-form-item>
      </el-form>

      <PureTableBar title="数据列表" :columns="columns" @refresh="onSearch">
        <template #buttons>
          <el-button
            type="success"
            :icon="useRenderIcon(AddFill)"
            plain
            @click="openDialog()"
          >
            新增
          </el-button>
        </template>
        <template v-slot="{ size, dynamicColumns }">
          <div
            v-if="selectedNum > 0"
            v-motion-fade
            class="bg-[var(--el-fill-color-light)] w-full h-[46px] mb-2 pl-4 flex items-center"
          >
            <div class="flex-auto">
              <span
                style="font-size: var(--el-font-size-base)"
                class="text-[rgba(42,46,54,0.5)] dark:text-[rgba(220,220,242,0.5)]"
              >
                已选 {{ selectedNum }} 项
              </span>
              <el-button type="primary" text @click="onSelectionCancel">
                取消选择
              </el-button>
            </div>
            <el-popconfirm title="是否确认删除?" @confirm="onBatchDel">
              <template #reference>
                <el-button type="danger" text class="mr-1">
                  批量删除
                </el-button>
              </template>
            </el-popconfirm>
          </div>
          <pure-table
            ref="tableRef"
            row-key="id"
            adaptive
            align-whole="center"
            table-layout="auto"
            :loading="loading"
            :size="size"
            :data="dataList"
            :columns="dynamicColumns"
            :pagination="pagination"
            :paginationSmall="size === 'small'"
            :default-sort="{ prop: 'id', order: 'ascending' }"
            :header-cell-style="{
              background: 'var(--el-fill-color-light)',
              color: 'var(--el-text-color-primary)'
            }"
            :border="false"
            stripe
            @selection-change="handleSelectionChange"
            @page-size-change="handleSizeChange"
            @page-current-change="handleCurrentChange"
            @sort-change="handleSortChange"
          >
            <template #operation="{ row }">
              <el-button
                class="reset-margin"
                link
                type="warning"
                :size="size"
                :icon="useRenderIcon(EditPen)"
                @click="openDialog('编辑', row)"
              >
                修改
              </el-button>
              <el-popconfirm
                :title="`是否确认删除 ID 为 ${row.id} 的这条数据`"
                @confirm="handleDelete(row)"
              >
                <template #reference>
                  <el-button
                    class="reset-margin"
                    link
                    type="danger"
                    :size="size"
                    :icon="useRenderIcon(Delete)"
                  >
                    删除
                  </el-button>
                </template>
              </el-popconfirm>
            </template>
          </pure-table>
        </template>
      </PureTableBar>
    </div>
  </div>
</template>
