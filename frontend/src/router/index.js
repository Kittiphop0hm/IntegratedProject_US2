import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import LandingPage from "../views/LandingPage.vue";
import SaleItemDetail from "../components/saleItem/SaleItemDetail.vue";
import SaleItemAddEdit from "../components/saleItem/SaleItemAddEdit.vue";
import SaleItemList from "../components/saleItem/SaleItemList.vue";
import BrandAddEdit from "@/components/brand/BrandAddEdit.vue";
import BrandManager from "@/components/brand/BrandManager.vue";

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
    path: '/sale-items/list/:id',
    name: 'SaleItemListById',
    component: SaleItemDetail,
  },
    {
      path: "/brands",
      name: "BrandManager",
      component: BrandManager,
    },
    {
      path: "/brands/add",
      name: "BrandAdd",
      component: BrandAddEdit,
    },
    {
      path: "/brands/:id/edit",
      name: "BrandEdit",
      component: BrandAddEdit,
    },
  ],
});



export default router;
