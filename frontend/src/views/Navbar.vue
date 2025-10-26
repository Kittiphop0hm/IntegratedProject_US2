<script setup>
import { addItemNoBodyAndNoContent } from "@/libs/fetchUtil";
import { ref, watchEffect } from "vue";
import { decodeJWT } from "@/libs/decodeJWT";
import { useRouter } from "vue-router";
const isBurgerBar = ref(false);
const isLoggedIn = ref(false);
const userNickname = ref(sessionStorage.getItem("nickname"));
const nickname = ref(userNickname.value || "");
const router = useRouter();
import { useUserStore } from "../stores/users.js";
import { useCartStore } from "@/stores/carts.js";
import { useCountNewOrder } from "@/stores/countNewOrder.js";

const userStore = useUserStore();
const cartStore = useCartStore();
const { isSeller } = useUserStore()
const { getCountNewOrder, fetchCountNewOrder, setCountNewOrder } = useCountNewOrder()
fetchCountNewOrder()
console.log(isSeller);
console.log(getCountNewOrder());



watchEffect(() => {
  if (nickname.value) {
    isLoggedIn.value = true;
  } else {
    isLoggedIn.value = false;
  }
});

const logout = async () => {
  const decodeSession = decodeJWT(sessionStorage.getItem("accessToken"));
  decodeSession.exp = 0;
  sessionStorage.removeItem("accessToken");
  sessionStorage.removeItem("nickname");
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
      class="w-full h-[90px] border-b border-gray-300 px-10 flex justify-between items-center"
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
          <router-link to="/" class="font-semibold">Home</router-link>
        </div>
        <div class="mx-3 hover:opacity-80">
          <router-link :to="{ name: 'SaleItemHome' }">Product</router-link>
        </div>
        <div class="mx-3 hover:opacity-80">
          <router-link :to="{ name: 'BrandManager' }">Manage Brand</router-link>
        </div>
        <div class="mx-3 hover:opacity-80 itbms-seller">
          <router-link :to="{ name: 'SaleItemList' }"
            >List SaleItem</router-link
          >
        </div>
        <div class="mx-3 hover:opacity-80">
          <router-link to="/">Contact</router-link>
        </div>
      </div>

      <div class="flex justify-center items-center max-md:hidden">
        <button class="mx-3 cursor-pointer">
          <svg
            class="fill-current hover:opacity-80"
            xmlns="http://www.w3.org/2000/svg"
            width="25"
            height="32"
            viewBox="0 0 24 24"
          >
            <path
              d="M16.5 3c-1.74 0-3.41.81-4.5 2.09C10.91 3.81 9.24 3 7.5 3C4.42 3 2 5.42 2 8.5c0 3.78 3.4 6.86 8.55 11.54L12 21.35l1.45-1.32C18.6 15.36 22 12.28 22 8.5C22 5.42 19.58 3 16.5 3m-4.4 15.55l-.1.1l-.1-.1C7.14 14.24 4 11.39 4 8.5C4 6.5 5.5 5 7.5 5c1.54 0 3.04.99 3.57 2.36h1.87C13.46 5.99 14.96 5 16.5 5c2 0 3.5 1.5 3.5 3.5c0 2.89-3.14 5.74-7.9 10.05"
            />
          </svg>
        </button>
        <router-link 
        :to="{ name: 'SaleItemCart' }">
          <button class="relative mx-3 cursor-pointer" 
          :disabled="cartStore.cartObj.length === 0">
          <svg
            class=" border fill-current hover:opacity-80"
            xmlns="http://www.w3.org/2000/svg"
            width="25"
            height="32"
            viewBox="0 0 24 24"
          >
            <path
              d="M15.55 13c.75 0 1.41-.41 1.75-1.03l3.58-6.49A.996.996 0 0 0 20.01 4H5.21l-.94-2H1v2h2l3.6 7.59l-1.35 2.44C4.52 15.37 5.48 17 7 17h12v-2H7l1.1-2zM6.16 6h12.15l-2.76 5H8.53zM7 18c-1.1 0-1.99.9-1.99 2S5.9 22 7 22s2-.9 2-2s-.9-2-2-2m10 0c-1.1 0-1.99.9-1.99 2s.89 2 1.99 2s2-.9 2-2s-.9-2-2-2"
            />
            
          </svg>
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
            <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 24 24"><path fill="#000000" d="m17.371 19.827l2.84-2.796l-.626-.627l-2.214 2.183l-.956-.975l-.627.632l1.583 1.583ZM6.77 8.73h10.462v-1H6.769v1ZM18 22.115q-1.671 0-2.836-1.164T14 18.115q0-1.67 1.164-2.835T18 14.115q1.671 0 2.836 1.165T22 18.115q0 1.672-1.164 2.836Q19.67 22.115 18 22.115ZM4 20.77V5.615q0-.67.472-1.143Q4.944 4 5.615 4h12.77q.67 0 1.143.472q.472.472.472 1.143v5.945q-.244-.09-.485-.154q-.24-.064-.515-.1v-5.69q0-.231-.192-.424Q18.615 5 18.385 5H5.615q-.23 0-.423.192Q5 5.385 5 5.615V19.05h6.344q.068.41.176.802q.109.392.303.748l-.035.035l-1.134-.827l-1.346.961l-1.346-.961l-1.347.961l-1.346-.961L4 20.769Zm2.77-4.5h4.709q.056-.275.138-.515q.083-.24.193-.485H6.77v1Zm0-3.769h7.31q.49-.387 1.05-.645q.56-.259 1.197-.355H6.769v1ZM5 19.05V5v14.05Z"/></svg>
          </button>
        </router-link>

        <router-link :to="{name: 'OrderHistory'}" class="w-full h-full tooltip tooltip-bottom" data-tip="Your orders">
          <button class="relative w-full h-full p-1 cursor-pointer hover:opacity-80">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 32 32"><path fill="#000000" d="M19 21h-6a3 3 0 0 0-3 3v2h2v-2a1 1 0 0 1 1-1h6a1 1 0 0 1 1 1v2h2v-2a3 3 0 0 0-3-3zm-3-1a4 4 0 1 0-4-4a4 4 0 0 0 4 4zm0-6a2 2 0 1 1-2 2a2 2 0 0 1 2-2z"/><path fill="#000000" d="M25 5h-3V4a2 2 0 0 0-2-2h-8a2 2 0 0 0-2 2v1H7a2 2 0 0 0-2 2v21a2 2 0 0 0 2 2h18a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2ZM12 4h8v4h-8Zm13 24H7V7h3v3h12V7h3Z"/></svg>
          </button>
        </router-link>

        <div class="flex items-center mx-3">
          <!-- ไอคอน User/Register อยู่เสมอ ไม่ว่าจะ login หรือไม่ -->
          <router-link v-if="!isLoggedIn" :to="{name: 'Register'}" class="cursor-pointer flex justify-center items-center mr-3">
            <button class="cursor-pointer hover:opacity-80">
              <svg
                class="fill-current hover:opacity-80"
                xmlns="http://www.w3.org/2000/svg"
                width="28"
                height="32"
                viewBox="0 0 24 24"
              >
                <path
                  d="M12 5.9a2.1 2.1 0 1 1 0 4.2a2.1 2.1 0 0 1 0-4.2m0 9c2.97 0 6.1 1.46 6.1 2.1v1.1H5.9V17c0-.64 3.13-2.1 6.1-2.1M12 4C9.79 4 8 5.79 8 8s1.79 4 4 4s4-1.79 4-4s-1.79-4-4-4m0 9c-2.67 0-8 1.34-8 4v2c0 .55.45 1 1 1h14c.55 0 1-.45 1-1v-2c0-2.66-5.33-4-8-4"
                />
              </svg>
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
                <router-link :to="{ name: 'UserProfile' }">Profile</router-link>
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

  <!-- Mobile Menu -->
  <div v-show="isBurgerBar" class="w-full h-full md:hidden">
    <div
      class="flex flex-col justify-center items-center border-b border-gray-300 p-3"
    >
      <div v-if="nickname" class="my-3 text-sm font-medium text-gray-700">
        Welcome, {{ nickname }}
      </div>

      <div class="my-3 hover:opacity-80">
        <router-link to="/">Home</router-link>
      </div>
      <div class="my-3 hover:opacity-80">
        <router-link to="/sale-items">Product</router-link>
      </div>
      <div class="my-3 hover:opacity-80">
        <router-link to="/">About</router-link>
      </div>
      <div class="my-3 hover:opacity-80">
        <router-link to="/sale-items/list">SaleItemList</router-link>
      </div>
      <div class="my-3 hover:opacity-80">
        <router-link to="/">Contact</router-link>
      </div>
      <div class="my-3 hover:opacity-80">
        <router-link to="/">Like</router-link>
      </div>
      <div class="my-3 hover:opacity-80">
        <router-link to="/"> Carts </router-link>
      </div>

      <div v-if="isLoggedIn">
        <div class="my-3 hover:opacity-80">
          <router-link :to="{ name: 'UserProfile' }">Profile</router-link>
        </div>
        <div class="my-3 hover:opacity-80">
          <a @click="logout" class="cursor-pointer text-red-600">Logout</a>
        </div>
      </div>
    </div>
  </div>
</template>
