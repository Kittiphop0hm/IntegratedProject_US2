import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import LandingPage from "../views/LandingPage.vue";
import SaleItemDetail from "../components/saleItem/SaleItemDetail.vue";
import SaleItemAddEdit from "../components/saleItem/SaleItemAddEdit.vue";
import SaleItemList from "../components/saleItem/SaleItemList.vue";
import BrandAddEdit from "@/components/brand/BrandAddEdit.vue";
import BrandManager from "@/components/brand/BrandManager.vue";
import RegisterAndLoginManager from "@/components/user/RegisterAndLoginManager.vue";
import EmailVerification from "@/components/user/EmailVerification.vue";
import UserProfileManager from "@/components/user/UserProfileManager.vue";
const accessToken = sessionStorage.getItem("accessToken");
import { decodeJWT } from "@/libs/decodeJWT.js";
const getUser = decodeJWT(accessToken);
import { computed } from "vue";
import { useUserStore } from "../stores/users.js";
import SaleItemCart from "@/components/saleItem/SaleItemCart.vue";
import OrderHistoryManager from "@/components/order/OrderHistoryManager.vue";
import OrderDetail from "@/components/order/OrderDetail.vue";



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
      path: "/sale-items/list",
      name: "SaleItemList",
      component: SaleItemList,
    },
    {
      path: "/sale-items/list/:id",
      name: "SaleItemListById",
      component: SaleItemDetail,
    },
    {
      path: "/sale-items/cart",
      name: "SaleItemCart",
      component: SaleItemCart ,
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
    {
      path: "/register",
      name: "Register",
      component: RegisterAndLoginManager,
    },
    {
      path: "/signin",
      name: "Login",
      component: RegisterAndLoginManager,
    },
    {
      path: "/verify-email",
      name: "EmailVerification",
      component: EmailVerification,
    },
    {
      path: "/profile",
      name: "UserProfile",
      component: UserProfileManager,
    },
    {
      path: "/profile/edit",
      name: "UserProfileEdit",
      component: UserProfileManager,
    },
    {
      path: "/sale-orders",
      name: "OrderHistory",
      component: OrderHistoryManager,
    },
    {
      path: "/your-orders/:orderId",
      name: "OrderDetail",
      component: OrderDetail,
    },
  ],
});

router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  const accessToken = sessionStorage.getItem("accessToken");
  const token = accessToken; // เพื่อให้ชื่อเดิมไม่ error

  console.log("from.name:", from.name);
  console.log("to.name:", to.name);
  console.log("userStore.role:", userStore.role);

  // 1️⃣ ถ้ามี token แล้ว และพยายามเข้า Login หรือ Register
  if (accessToken && (to.name === "Login" || to.name === "Register")) {
    // 👉 กลับไปหน้าเดิม (ไม่เปลี่ยนหน้า)
    next(false);
    return;
  }

  // 2️⃣ ถ้าไม่มี token แล้วพยายามเข้าหน้า SaleItemList (ยกเว้นมาจากหน้า Login)
  if (to.name === "SaleItemList" && !accessToken && from.name !== "Login") {
    next({ name: "Login" });
    return;
  }

  // 3️⃣ ถ้าเข้าหน้า SaleItemList แต่ไม่ใช่ Seller → เด้งกลับ Home
  if (to.name === "SaleItemList" && !userStore.isSeller) {
    next({ name: "SaleItemHome" });
    return;
  }

  // 4️⃣ ถ้ามาจากหน้า SaleItemHome แล้วไปหน้าอื่น (ยกเว้น SaleItemDetail)
  if (from.name === "SaleItemHome" && to.name !== "SaleItemDetail") {
    sessionStorage.removeItem("pageSize");
    sessionStorage.removeItem("pageNumber");
  }

  // ✅ ผ่านหมดทุกเงื่อนไข → ไปต่อได้เลย
  next();
});

export default router;
