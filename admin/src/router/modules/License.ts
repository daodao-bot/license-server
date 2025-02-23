const Layout = () => import("@/layout/index.vue");

export default {
  path: "/license",
  name: "License",
  component: Layout,
  redirect: "/license/index",
  meta: {
    icon: "ep:collection",
    title: "许可证管理",
    rank: 4
  },
  children: [
    {
      path: "/license/index",
      name: "LicenseIndex",
      component: () => import("@/views/license/index.vue"),
      meta: {
        icon: "ep:memo",
        title: "许可证",
        keepAlive: true,
        showParent: true
      }
    }
  ]
} satisfies RouteConfigsTable;
