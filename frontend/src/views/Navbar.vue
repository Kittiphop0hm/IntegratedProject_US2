<script setup>
import { addItemNoBodyAndNoContent } from "@/libs/fetchUtil";
import { ref, watchEffect } from "vue";
import { decodeJWT } from "@/libs/decodeJWT";
import { useRouter } from "vue-router";

const isBurgerBar = ref(false);
const isLoggedIn = ref(false);
const userNickname = ref(localStorage.getItem("nickname"));
const nickname = ref(userNickname.value || "");
const router = useRouter();
import { useUserStore } from "../stores/users.js";
import { useCartStore } from "@/stores/carts.js";
import { useCountNewOrder } from "@/stores/countNewOrder.js";
const userStore = useUserStore()
const cartStore = useCartStore()
const { isSeller } = useUserStore()
const { getCountNewOrder, fetchCountNewOrder, setCountNewOrder } = useCountNewOrder()

fetchCountNewOrder()

watchEffect(() => {
  if (nickname.value) {
    isLoggedIn.value = true;
  } else {
    isLoggedIn.value = false;
  }
});

const logout = async () => {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("nickname");
  nickname.value = "";
  userStore.clearUser();
  cartStore.clearCart();
  const logout = await addItemNoBodyAndNoContent(
    `${import.meta.env.VITE_APP_URL}/v2/auth/logout`
  );
  setCountNewOrder(0)
  router.push({ name: "SaleItemHome" });
};
</script>

<template>
  <nav class="w-full max-h-[90px]">
    <div
      class="fixed z-[98] top-0 w-full h-[90px] bg-linear-to-r from-[#2D1E17] to-[#796254] text-white px-10 flex justify-between items-center"
    >
      <router-link to="/">
        <h1 class="font-bold text-xl cursor-pointer hover:opacity-70">
          ITB-MSHOP
        </h1>
      </router-link>

      <div
        class="flex justify-center items-center text-lg m-auto max-md:hidden"
      >
        <div class="mx-3 hover:opacity-80">
          <router-link to="/">Home</router-link>
        </div>
        <div class="mx-3 hover:opacity-80">
          <router-link :to="{ name: 'SaleItemHome' }">Sale Items</router-link>
        </div>
        <div v-show="useUserStore.role === 'Admin'" class="mx-3 hover:opacity-80">
          <router-link :to="{ name: 'BrandManager' }">Brand</router-link>
        </div>
        <div class="mx-3 hover:opacity-80 itbms-seller">
          <router-link :to="{ name: 'SaleItemList' }">My Sale Item</router-link>
        </div>
      </div>

      <div class="flex justify-center items-center max-md:hidden">
        <router-link 
        :to="{ name: 'SaleItemCart' }">
          <button class="flex justify-center items-center relative mx-3 cursor-pointer" 
          :disabled="cartStore.cartObj.length === 0">
            <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 24 24"><path fill="#ffffff" d="M17 18c-1.11 0-2 .89-2 2a2 2 0 0 0 2 2a2 2 0 0 0 2-2a2 2 0 0 0-2-2M1 2v2h2l3.6 7.59l-1.36 2.45c-.15.28-.24.61-.24.96a2 2 0 0 0 2 2h12v-2H7.42a.25.25 0 0 1-.25-.25c0-.05.01-.09.03-.12L8.1 13h7.45c.75 0 1.41-.42 1.75-1.03l3.58-6.47c.07-.16.12-.33.12-.5a1 1 0 0 0-1-1H5.21l-.94-2M7 18c-1.11 0-2 .89-2 2a2 2 0 0 0 2 2a2 2 0 0 0 2-2a2 2 0 0 0-2-2Z"/></svg>
          <div
          v-if="cartStore.cartQuantity > 0"
            class="itbms-cart-quantity top-[-8px] left-3 absolute inline-block bg-red-500 text-white text-xs font-bold rounded-full px-2 py-1 ml-1"
          >
            {{ cartStore.cartQuantity }}
          </div>
        </button>
        </router-link>

        <router-link v-if="isSeller" :to="{name: 'SellerOrderHistory'}" class="w-full h-full tooltip tooltip-bottom" data-tip="Sale orders">
          <button class="relative w-full h-full p-1 cursor-pointer hover:opacity-80">
            <div v-if="getCountNewOrder() > 0" class="w-4 h-4 absolute top-0 right-0 bg-red-400 rounded-full">
              <p class="text-[12px]">{{ getCountNewOrder() }}</p>
            </div>
            <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 24 24"><path fill="#ffffff" d="m17.371 19.827l2.84-2.796l-.626-.627l-2.214 2.183l-.956-.975l-.627.632l1.583 1.583ZM6.77 8.73h10.462v-1H6.769v1ZM18 22.115q-1.671 0-2.836-1.164T14 18.115q0-1.67 1.164-2.835T18 14.115q1.671 0 2.836 1.165T22 18.115q0 1.672-1.164 2.836Q19.67 22.115 18 22.115ZM4 20.77V5.615q0-.67.472-1.143Q4.944 4 5.615 4h12.77q.67 0 1.143.472q.472.472.472 1.143v5.945q-.244-.09-.485-.154q-.24-.064-.515-.1v-5.69q0-.231-.192-.424Q18.615 5 18.385 5H5.615q-.23 0-.423.192Q5 5.385 5 5.615V19.05h6.344q.068.41.176.802q.109.392.303.748l-.035.035l-1.134-.827l-1.346.961l-1.346-.961l-1.347.961l-1.346-.961L4 20.769Zm2.77-4.5h4.709q.056-.275.138-.515q.083-.24.193-.485H6.77v1Zm0-3.769h7.31q.49-.387 1.05-.645q.56-.259 1.197-.355H6.769v1ZM5 19.05V5v14.05Z"/></svg>
          </button>
        </router-link>

        <router-link v-if="isLoggedIn" :to="{name: 'OrderHistory'}" class="w-full h-full tooltip tooltip-bottom" data-tip="Your orders">
          <button class="relative w-full h-full p-1 cursor-pointer hover:opacity-80">
            <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 32 32"><path fill="#ffffff" d="M19 21h-6a3 3 0 0 0-3 3v2h2v-2a1 1 0 0 1 1-1h6a1 1 0 0 1 1 1v2h2v-2a3 3 0 0 0-3-3zm-3-1a4 4 0 1 0-4-4a4 4 0 0 0 4 4zm0-6a2 2 0 1 1-2 2a2 2 0 0 1 2-2z"/><path fill="#ffffff" d="M25 5h-3V4a2 2 0 0 0-2-2h-8a2 2 0 0 0-2 2v1H7a2 2 0 0 0-2 2v21a2 2 0 0 0 2 2h18a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2ZM12 4h8v4h-8Zm13 24H7V7h3v3h12V7h3Z"/></svg>
          </button>
        </router-link>

        <div class="flex items-center mx-3">
          <!-- ไอคอน User/Register อยู่เสมอ ไม่ว่าจะ login หรือไม่ -->
          <router-link v-if="!isLoggedIn" :to="{name: 'Register'}" class="cursor-pointer flex justify-center items-center mr-3">
            <button class="cursor-pointer hover:opacity-80">
              <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 24 24" fill="#ffffff"><g fill="none" stroke="#ffffff" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><circle cx="12" cy="8" r="5" fill="#ffffff"/><path d="M20 21a8 8 0 1 0-16 0"/><path fill="#ffffff" d="M12 13a8 8 0 0 0-8 8h16a8 8 0 0 0-8-8z"/></g></svg>
            </button>
          </router-link>

          <div v-if="isLoggedIn" class="dropdown dropdown-center">
            <div tabindex="0" role="button" class="btn m-1">
              {{ nickname }}
            </div>
            <ul
              tabindex="0"
              class="dropdown-content menu bg-base-100 rounded-box z-[1] w-52 p-2 shadow-sm"
            >
              <li>
                <router-link :to="{ name: 'UserProfile' }" class="text-black">Profile</router-link>
              </li>
              <li>
                <a
                  @click="logout"
                  class="cursor-pointer text-red-600 hover:bg-red-50"
                  >Logout</a
                >
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div class="flex justify-center items-center md:hidden">
        <button @click="isBurgerBar = !isBurgerBar" class="cursor-pointer">
          <svg
            class="fill-current hover:opacity-80"
            xmlns="http://www.w3.org/2000/svg"
            width="25"
            height="32"
            viewBox="0 0 24 24"
          >
            <path d="M2 15.5v2h20v-2zm0-5v2h20v-2zm0-5v2h20v-2z" />
          </svg>
        </button>
      </div>
    </div>
  </nav>

  <div v-show="isBurgerBar" class="fixed z-[90] top-5 w-full h-full md:hidden">
    <div
      class="flex flex-col justify-center items-center bg-linear-to-r from-[#2D1E17] to-[#796254] text-white p-3"
    >
      <div v-if="nickname" class="my-3 text-sm font-medium text-gray-700">
        Welcome, {{ nickname }}
      </div>

      <div class="my-3 hover:opacity-80">
        <router-link to="/">Home</router-link>
      </div>
      <div class="my-3 hover:opacity-80">
        <router-link to="/sale-items">SaleItems</router-link>
      </div>
      <div class="my-3 hover:opacity-80">
        <router-link :to="{name: 'BrandManager'}">Brand</router-link>
      </div>
      <div class="my-3 hover:opacity-80">
        <router-link to="/sale-items/list">My SaleItem</router-link>
      </div>
      <div class="my-3 hover:opacity-80">
        <router-link :to="{name: 'OrderHistory'}">Carts</router-link>
      </div>
      <div v-if="!isLoggedIn" class="my-3 hover:opacity-80">
        <router-link :to="{name: 'Register'}">SignIn</router-link>
      </div>
      <div v-if="!isLoggedIn" class="my-3 hover:opacity-80">
        <router-link :to="{name: 'Login'}">SignUp</router-link>
      </div>

      <div v-if="isLoggedIn" class="flex flex-col justify-center items-center">
        <div class="my-3 hover:opacity-80">
          <router-link :to="{ name: 'UserProfile' }">Profile</router-link>
        </div>
        <div class="my-3 hover:opacity-80">
          <a @click="logout" class="cursor-pointer">Logout</a>
        </div>
      </div>
    </div>
  </div>
</template>
