const Layout = () => import("@/layout/index.vue");

export default {
  path: "/customer",
  name: "Customer",
  component: Layout,
  redirect: "/customer/index",
  meta: {
    icon: "ep:collection",
    title: "客户管理",
    rank: 3
  },
  children: [
    {
      path: "/customer/index",
      name: "CustomerIndex",
      component: () => import("@/views/customer/index.vue"),
      meta: {
        icon: "ep:memo",
        title: "客户",
        keepAlive: true,
        showParent: true
      }
    }
  ]
} satisfies RouteConfigsTable;
