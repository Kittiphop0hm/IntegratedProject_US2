<script setup>
import { onMounted } from 'vue';
import Navbar from './Navbar.vue'
import SaleItemGallery from '../components/SaleItemGallery.vue'
import {getItems} from '../libs/fetchUtil'
import { ref } from 'vue';

const myProducts = ref([])

onMounted( async () => {
    try{
        myProducts.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items`)
        console.log(myProducts.value)
    }
    catch(error){
       console.log(error)
        
    }
})

</script>

<template>
  <Navbar />

  <div class="mt-16 flex items-center justify-center w-full">
    <div class="flex items-center space-x-4 w-full max-w-4xl px-8">
      <h1 class="text-2xl font-bold text-gray-800 whitespace-nowrap">
        ITBMS Shop
      </h1>

      <div class="flex flex-1 space-x-2">
        <input
          type="text"
          placeholder="Search for products..."
          class="px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 w-full"
        />
        <button class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600">
          Search
        </button>
      </div>

      <!-- Add Profile and Cart Icons -->
      <div class="flex space-x-4">
        <div class="cursor-pointer">
          <i class="fas fa-user text-gray-600 text-2xl"></i>
        </div>
        <div class="cursor-pointer">
          <i class="fas fa-shopping-cart text-gray-600 text-2xl"></i>
        </div>
      </div>
    </div>
  </div>
  <SaleItemGallery />


</template>

