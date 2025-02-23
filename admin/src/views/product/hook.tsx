import dayjs from "dayjs";
import { type Ref, ref } from "vue";

import { ElMessageBox } from "element-plus";

import { message } from "@/utils/message";

import { usePublicHooks } from "@/views/system/hooks";

import type { ProductData, ProductUpsert } from "@/api/product";
import { productUpsert } from "@/api/product";
import { emptyToNull } from "@/utils/license/common";

export function useProduct() {
  const switchLoadMap: Ref<any[]> = ref([]);
  const { switchStyle } = usePublicHooks();
  const onChange = ({ row, index }: { row: ProductData; index: number }) => {
    ElMessageBox.confirm(
      `确认要 <strong>${
        row.valid === true ? "启用" : "停用"
      }</strong> id = <strong style='color:var(--el-color-primary)'>${
        row.id
      }</strong> 的数据吗?`,
      "系统提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
        draggable: true
      }
    )
      .then(() => {
        switchLoadMap.value[index] = Object.assign(
          {},
          switchLoadMap.value[index],
          {
            loading: true
          }
        );
        setTimeout(() => {
          const param: ProductUpsert = emptyToNull({
            id: row.id,
            ...row
          }) as ProductUpsert;
          productUpsert({ param })
            .then(({ data }) => {
              console.debug("data", data);
              switchLoadMap.value[index] = Object.assign(
                {},
                switchLoadMap.value[index],
                {
                  loading: false
                }
              );
              message("已成功修改数据状态", {
                type: "success"
              });
            })
            .catch(() => {
              switchLoadMap.value[index] = Object.assign(
                {},
                switchLoadMap.value[index],
                {
                  loading: false
                }
              );
              row.valid === true ? (row.valid = false) : (row.valid = true);
            });
        }, 300);
      })
      .catch(() => {
        row.valid === true ? (row.valid = false) : (row.valid = true);
      });
  };

  const columns: TableColumnList = [
    {
      label: "勾选列", // 如果需要表格多选，此处label必须设置
      type: "selection",
      fixed: "left",
      reserveSelection: true // 数据刷新后保留选项
    },
    {
      label: "ID",
      prop: "id",
      width: 90,
      sortable: "custom"
    },
    {
      label: "代码",
      prop: "code",
      minWidth: 130
    },
    {
      label: "名称",
      prop: "name",
      minWidth: 130
    },
    {
      label: "状态",
      prop: "valid",
      minWidth: 90,
      cellRenderer: scope => (
        <el-switch
          size={scope.props.size === "small" ? "small" : "default"}
          loading={switchLoadMap.value[scope.index]?.loading}
          v-model={scope.row.valid}
          active-value={true}
          inactive-value={false}
          active-text="启用"
          inactive-text="停用"
          inline-prompt
          style={switchStyle.value}
          onChange={() => onChange(scope as any)}
        />
      )
    },
    {
      label: "创建时间",
      minWidth: 60,
      prop: "createTime",
      sortable: true,
      formatter: ({ createTime }) =>
        dayjs(createTime).format("YYYY-MM-DD HH:mm:ss")
    },
    {
      label: "更新时间",
      minWidth: 60,
      prop: "updateTime",
      formatter: ({ updateTime }) =>
        dayjs(updateTime).format("YYYY-MM-DD HH:mm:ss")
    },
    {
      label: "操作",
      fixed: "right",
      width: 180,
      slot: "operation"
    }
  ];

  return {
    columns
  };
}
