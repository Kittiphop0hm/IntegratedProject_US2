import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import LandingPage from "../views/LandingPage.vue";
import SaleItemDetail from "../components/saleItem/SaleItemDetail.vue";
import SaleItemAddEdit from "../components/saleItem/SaleItemAddEdit.vue";
import SaleItemList from "../components/saleItem/SaleItemList.vue";

import BrandManager from "@/components/brand/BrandManager.vue";
import BrandAdd from "@/components/brand/BrandAdd.vue";
import BrandEdit from "@/components/brand/BrandEdit.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: LandingPage,
    },
    {
      path: "/sale-items",
      // name: "sale-items-home",
      name: "SaleItemHome",
      component: HomeView,
    },
    {
      path: "/sale-items/:id",
      name: "SaleItemDetail",
      component: SaleItemDetail,
    },
    {
      path: "/sale-items/add",
      name: "SaleItemAdd",
      component: SaleItemAddEdit,
    },
    {
      path: "/sale-items/:id/edit",
      name: "SaleItemEdit",
      component: SaleItemAddEdit,
    },
 {
    path: '/sale-items/list',
    name: 'SaleItemList',
    component: SaleItemList,
  },
    {
      path: "/brands",
      name: "BrandList",
      component: BrandManager,
    },
    {
      path: "/brands/add",
      name: "BrandAdd",
      component: BrandAdd,
    },
    {
      path: "/brands/:id/edit",
      name: "BrandEdit",
      component: BrandEdit,
    },
  ],
});

export default router;
