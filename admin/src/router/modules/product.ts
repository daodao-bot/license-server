const Layout = () => import("@/layout/index.vue");

export default {
  path: "/product",
  name: "Product",
  component: Layout,
  redirect: "/product/index",
  meta: {
    icon: "ep:collection",
    title: "产品管理",
    rank: 2
  },
  children: [
    {
      path: "/product/index",
      name: "ProductIndex",
      component: () => import("@/views/product/index.vue"),
      meta: {
        icon: "ep:memo",
        title: "产品",
        keepAlive: true,
        showParent: true
      }
    }
  ]
} satisfies RouteConfigsTable;
