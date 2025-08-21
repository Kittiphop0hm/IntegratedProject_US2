<script setup>
import Search from '../Search.vue';
import { ref, onMounted } from 'vue';
import { getItems } from '@/libs/fetchUtil';
import { useRoute } from 'vue-router';

const route = useRoute();
const emit = defineEmits(["fetchImagesForUpdate"])

const props = defineProps({
    isActive: {
        type: Boolean ,
        default: true
    },
    isUpdated: {
        type: Boolean ,
        default: true
    },
    saleId: {
        type:Number
    },
    isUpdateMode: {
        type: Boolean ,
        default: false
    },
    isAddMode: {
        type: Boolean ,
        default: false
    },
})

const pictures = ref([])
const items = ref([])
const mainImage = ref("")

onMounted(async () => {
  try {
    if (route.params.id) {
      items.value = await getItems(`${import.meta.env.VITE_APP_URL}/api/files/imageSale/${route.params.id}`)
      console.log(items.value);
      emit("fetchImagesForUpdate", items.value)
      if (items.value.length > 0) {
        items.value.forEach((item) => {
          const apiFormat = `${import.meta.env.VITE_APP_URL}/api/files/${item.fileName}`
          pictures.value.push(apiFormat)
        })
      }
      mainImage.value = pictures.value[0]
    }
  } catch(err) {
    console.error(err);
  }
})

const clickShowImage = (index) => {  
  mainImage.value = pictures.value[index]
}
</script>

<template>
    <Search/>
    <div class=" container mx-auto px-6 mt-10 mb-10">
      <div class="text-gray-800 bg-white bg-opacity-70 p-2 mb-4">
        <p>
          <router-link 
            :to="{ name: 'SaleItemHome' }">
            <span class="itbms-home-button text-blue-600">Home</span>
          </router-link> &gt;
          <span class=" font-semibold"> 
              <slot name="path"></slot>
          </span>
        </p>
      </div>
    
    <div class="itbms-row flex flex-col lg:flex-row lg:space-x-10">
      <div class="lg:w-1/2">
        <div class="relative rounded-md p-2 bg-white">
          <!-- <button class="absolute top-1 right-2 text-red-500 font-bold cursor-pointer hover:opacity-70">X</button> -->
          <img :src="mainImage" alt="main image" class="w-full" />
        </div>

        <div class="grid grid-cols-4 gap-2 mt-4">
          <div @click="clickShowImage(0)" class="relative border border-gray-300 rounded p-1 hover:border-gray-400 cursor-pointer">
            <!-- <button class="absolute top-0 right-1 text-red-500 font-bold cursor-pointer hover:opacity-70">X</button> -->
            <img :src="pictures[0]" alt="thumb 1" class="w-30 mx-auto" />
          </div>
          <div @click="clickShowImage(1)" class="relative border border-gray-300 rounded p-1 hover:border-gray-400 cursor-pointer">
            <!-- <button class="absolute top-0 right-1 text-red-500 font-bold cursor-pointer hover:opacity-70">X</button> -->
            <img :src="pictures[1]" alt="thumb 2" class="w-30 mx-auto" />
          </div>
          <div @click="clickShowImage(2)" class="relative border border-gray-300 rounded p-1 hover:border-gray-400 cursor-pointer">
            <!-- <button class="absolute top-0 right-1 text-red-500 font-bold cursor-pointer hover:opacity-70">X</button> -->
            <img :src="pictures[2]" alt="thumb 3" class="w-30 mx-auto" />
          </div>
          <div @click="clickShowImage(3)" class="relative border border-gray-300 rounded p-1 hover:border-gray-400 cursor-pointer">
            <!-- <button class="absolute top-0 right-1 text-red-500 font-bold cursor-pointer hover:opacity-70">X</button> -->
            <img :src="pictures[3]" alt="thumb 4" class="w-30 mx-auto" />
          </div>
        </div>
      </div>

      <div class="lg:w-1/2 mt-8 lg:mt-0">
        <p class="text-lg mt-2">Brand: 
            <slot name="brand">Input Brand Name</slot>
        </p>
        <p class=" text-lg mt-2">Model: 
            <slot name="model">input Model</slot>
        </p>
        <p class="text-lg  mt-2">
            <slot name="price">Input Price</slot>
        </p>
        <p class=" text-lg mt-2">
          Description: <br />
          <slot name="desc">Input Description</slot>
        </p>
        <p class=" text-lg mt-2">RAM: 
            <slot name="ram">Input Ram and Unit</slot>
        </p>
        <p class=" text-lg mt-2">
          Screen size: 
            <slot name="screen">Input Screen Size</slot>
        </p>
        <p class=" text-lg mt-2">
          Storage: 
            <slot name="storage">Input Storage</slot>
        </p>
        <p class=" text-lg mt-2">Color:
            <slot name="color">Input Color</slot>   
        </p>
        <p class=" text-lg mt-2">
          Available quantity: 
            <slot name="quantity">Input Quantity</slot>
        </p>
        <div class="">
        <button 
          class="mr-3 rounded-md mt-3 text-white"
          :class="{ 'bg-blue-500': isActive && isUpdated, 'bg-gray-500 opacity-75 cursor-not-allowed': !isActive  || !isUpdated}"
          :disabled="!isActive || !isUpdated"
          >
          <slot name="button1">Input Name Button1</slot>
        </button
        >
          <button class=" px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 cursor-pointer">
          <slot name="button2">Input Name Button2</slot>
        </button>
        </div>
      </div>
    </div>
  </div>
</template>
