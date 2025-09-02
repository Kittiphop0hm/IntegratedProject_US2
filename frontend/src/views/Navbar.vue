<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const isBurgerBar = ref(false)
const route = useRoute();
const nickname = ref('');

const getNickname = () => {
  return sessionStorage.getItem('nickname');
};

onMounted(() => {
  nickname.value = getNickname() || '';
});

</script>

<template>
  <nav class="w-full max-h-[90px] ">
    <div class="w-full h-[90px] border-b border-gray-300 px-10 flex justify-between items-center">
      <router-link to="/">
          <h1 class="font-bold text-xl cursor-pointer hover:opacity-70">ITB-MSHOP</h1>
      </router-link>

      <div class="flex justify-center items-center text-lg m-auto max-md:hidden">
        <div class="mx-3 hover:opacity-80">
          <router-link to="/" class="font-semibold">Home</router-link>
        </div>
        <div class="mx-3 hover:opacity-80">
          <router-link :to="{ name: 'SaleItemHome'  }">Product</router-link>
        </div>
        <div class="mx-3 hover:opacity-80">
          <router-link :to="{ name: 'BrandManager'  }">Manage Brand</router-link>
        </div>
        <div class="mx-3 hover:opacity-80 itbms-seller">
          <router-link :to="{ name: 'SaleItemList'  }">List SaleItem</router-link>
        </div>
        <div class="mx-3 hover:opacity-80">
          <router-link to="/">Contact</router-link>
        </div>
      </div>

      <div class="flex justify-center items-center max-md:hidden">
        <button class="mx-3 cursor-pointer">
            <svg class="fill-current hover:opacity-80" xmlns="http://www.w3.org/2000/svg" width="25" height="32" viewBox="0 0 24 24"><!-- Icon from Google Material Icons by Material Design Authors - https://github.com/material-icons/material-icons/blob/master/LICENSE --><path d="M16.5 3c-1.74 0-3.41.81-4.5 2.09C10.91 3.81 9.24 3 7.5 3C4.42 3 2 5.42 2 8.5c0 3.78 3.4 6.86 8.55 11.54L12 21.35l1.45-1.32C18.6 15.36 22 12.28 22 8.5C22 5.42 19.58 3 16.5 3m-4.4 15.55l-.1.1l-.1-.1C7.14 14.24 4 11.39 4 8.5C4 6.5 5.5 5 7.5 5c1.54 0 3.04.99 3.57 2.36h1.87C13.46 5.99 14.96 5 16.5 5c2 0 3.5 1.5 3.5 3.5c0 2.89-3.14 5.74-7.9 10.05"/></svg>
        </button>
        <button class="mx-3 cursor-pointer">
            <svg class="fill-current hover:opacity-80" xmlns="http://www.w3.org/2000/svg" width="25" height="32" viewBox="0 0 24 24"><!-- Icon from Google Material Icons by Material Design Authors - https://github.com/material-icons/material-icons/blob/master/LICENSE --><path d="M15.55 13c.75 0 1.41-.41 1.75-1.03l3.58-6.49A.996.996 0 0 0 20.01 4H5.21l-.94-2H1v2h2l3.6 7.59l-1.35 2.44C4.52 15.37 5.48 17 7 17h12v-2H7l1.1-2zM6.16 6h12.15l-2.76 5H8.53zM7 18c-1.1 0-1.99.9-1.99 2S5.9 22 7 22s2-.9 2-2s-.9-2-2-2m10 0c-1.1 0-1.99.9-1.99 2s.89 2 1.99 2s2-.9 2-2s-.9-2-2-2"/></svg>
        </button>
        
       
        <div class="flex items-center mx-3">

          <router-link :to="{name: 'Register'}" class="cursor-pointer flex justify-center items-center">
            <button class="cursor-pointer hover:opacity-80">
                <svg class="fill-current hover:opacity-80" xmlns="http://www.w3.org/2000/svg" width="28" height="32" viewBox="0 0 24 24"><!-- Icon from Google Material Icons by Material Design Authors - https://github.com/material-icons/material-icons/blob/master/LICENSE --><path d="M12 5.9a2.1 2.1 0 1 1 0 4.2a2.1 2.1 0 0 1 0-4.2m0 9c2.97 0 6.1 1.46 6.1 2.1v1.1H5.9V17c0-.64 3.13-2.1 6.1-2.1M12 4C9.79 4 8 5.79 8 8s1.79 4 4 4s4-1.79 4-4s-1.79-4-4-4m0 9c-2.67 0-8 1.34-8 4v2c0 .55.45 1 1 1h14c.55 0 1-.45 1-1v-2c0-2.66-5.33-4-8-4"/></svg>
            </button>
          </router-link>
          <span v-if="nickname" class="mr-3 text-sm font-medium text-gray-700 pl-3">
             {{ nickname }}
          </span>
        </div>
      </div>

      <div class="flex justify-center items-center md:hidden">
        <button @click="isBurgerBar = !isBurgerBar" class="cursor-pointer">
          <svg class="fill-current hover:opacity-80" xmlns="http://www.w3.org/2000/svg" width="25" height="32" viewBox="0 0 24 24"><!-- Icon from Google Material Icons by Material Design Authors - https://github.com/material-icons/material-icons/blob/master/LICENSE --><path d="M2 15.5v2h20v-2zm0-5v2h20v-2zm0-5v2h20v-2z"/></svg>
        </button>
      </div>
    </div>
  </nav>

  <div v-show="isBurgerBar" class="w-full h-full md:hidden">
    <div class="flex flex-col justify-center items-center border-b border-gray-300 p-3">
       
        <div v-if="nickname" class="my-3 text-sm font-medium text-gray-700">
          {{ nickname }}
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
          <router-link to="/">Carts</router-link>
        </div>
        <div class="my-3 hover:opacity-80">
          <router-link :to="{name: 'Register'}">Sign up</router-link>
        </div>
        <div class="my-3 hover:opacity-80">
          <router-link :to="{name: 'Login'}">Login</router-link>
        </div>
    </div>
  </div>
</template>