<script setup>
import Search from '../Search.vue';
import { useRouter } from 'vue-router';
const router = useRouter();
const props = defineProps({
    isActive: {
        type: Boolean ,
        default: true
    },
    isUpdated: {
        type: Boolean ,
        default: true
    }
})
</script>

<template>
    <Search/>
  <!-- Detail Section -->
  <div class=" container mx-auto px-6 mt-10 mb-10">
    <!-- Breadcrumb -->
    <div class="text-gray-800 bg-white bg-opacity-70 p-2 mb-4">
      <p>
        <router-link 
          :to="{ name: 'SaleItemHome' }">
          <span class="itbms-home-button text-blue-600">Home</span>
        </router-link> &gt;
        <span class="itbms-model itbms-color font-semibold"> 
            <slot name="path"></slot>
        </span>
      </p>
    </div>

    
    <div class="itbms-row flex flex-col lg:flex-row lg:space-x-10">
     
      <div class="lg:w-1/2">
        <div class=" rounded-md p-2 bg-white">
          <img src="/images/iPhone14ProMax.jpg" alt="main image" class="w-full " />
        </div>

        <div class="grid grid-cols-4 gap-2 mt-4">
          <div class="border border-gray-300 rounded p-1">
            <img src="/images/iPhone14ProMax.jpg" alt="thumb 1" class="w-30 mx-auto" />
          </div>
          <div class="border border-gray-300 rounded p-1">
            <img src="/images/iPhone14ProMax.jpg" alt="thumb 2" class="w-30 mx-auto" />
          </div>
          <div class="border border-gray-300 rounded p-1">
            <img src="/images/iPhone14ProMax.jpg" alt="thumb 3" class="w-30 mx-auto" />
          </div>
          <div class="border border-gray-300 rounded p-1">
            <img src="/images/iPhone14ProMax.jpg" alt="thumb 4" class="w-30 mx-auto" />
          </div>
        </div>
      </div>

      <div class="lg:w-1/2 mt-8 lg:mt-0">
        <p class="itbms-brand text-lg mt-2">Brand: 
            <slot name="brand">Input Brand Name</slot>
        </p>
        <p class="itbms-model text-lg mt-2">Model: 
            <slot name="model">input Model</slot>
        </p>
        <p class="itbms-price text-lg text-blue-600 mt-2">
            <slot name="price">Input Price</slot>
        </p>
        <p class="itbms-description text-lg mt-2">
          Description: <br />
          <slot name="desc">Input Description</slot>
        </p>
        <p class="itbms-ramGb text-lg mt-2">RAM: 
            <slot name="ram">Input Ram and Unit</slot>
        </p>
        <p class="itbms-screenSizeInch text-lg mt-2">
          Screen size: 
            <slot name="screen">Input Screen Size</slot>
        </p>
        <p class="itbms-storageGb text-lg mt-2">
          Storage: 
            <slot name="storage">Input Storage</slot>
        </p>
        <p class="itbms-color text-lg mt-2">Color:
            <slot name="color">Input Color</slot>   
        </p>
        <p class="itbms-quantity text-lg mt-2">
          Available quantity: 
            <slot name="quantity">Input Quantity</slot>
        </p>
        <div class="">
        <button 
        class="mr-3 itbms-save-button px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 mt-3"
        :class="{ 'bg-blue-500': isActive && isUpdated, 'bg-blue-500 opacity-75': !isActive  || !isUpdated}"
        >
        <slot name="button1">Input Name Button1</slot>
        </button
        >
          <button class="itbms-delete-button px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 cursor-pointer">
          <slot name="button2">Input Name Button2</slot>
        </button>
        </div>
      </div>
    </div>
  </div>
</template>
