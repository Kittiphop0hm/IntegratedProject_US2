<script setup>
import { useRoute } from 'vue-router'
import Navbar from '../components/Navbar.vue';

const route = useRoute()

const mockItems = [
  {
    id: 1,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'iPhone 14 Pro Max',
    brandName: 'Apple',
    ramGb: 6,
    storageGb: 512,
    price: 42900,
    color: 'Silver',
    quantity: 50,
    screenSizeInch: 6.7,
    description: 'The latest iPhone model powered by the A16 Bionic chip and Pro camera system.'
  },
  {
    id: 2,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'Galaxy S23 Ultra',
    brandName: 'Samsung',
    ramGb: 12,
    storageGb: 512,
    price: 32900,
    color: 'Phantom Black',
    quantity: 30,
    screenSizeInch: 6.8,
    description: 'Experience ultimate photography with a 200MP camera and built-in S Pen.'
  },
  {
    id: 3,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'Xiaomi 13',
    brandName: 'Xiaomi',
    ramGb: 8,
    storageGb: 256,
    price: 22900,
    color: 'Flora Green',
    quantity: 40,
    screenSizeInch: 6.36,
    description: 'Xiaomi flagship co-engineered with Leica, featuring ultra-fast 67W charging.'
  },
  {
    id: 4,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'iPhone SE 2022',
    brandName: 'Apple',
    ramGb: 4,
    storageGb: 64,
    price: 14190,
    color: 'Product Red',
    quantity: 100,
    screenSizeInch: 4.7,
    description: 'Compact and powerful, powered by the A15 Bionic chip with 5G support.'
  },
  {
    id: 5,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'OnePlus 11',
    brandName: 'OnePlus',
    ramGb: 16,
    storageGb: 256,
    price: 25900,
    color: 'Titan Black',
    quantity: 25,
    screenSizeInch: 6.7,
    description: 'Smooth performance with Snapdragon 8 Gen 2 and 100W superfast charging.'
  },
  {
    id: 6,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'iPhone SE 2022',
    brandName: 'Apple',
    ramGb: 4,
    storageGb: 64,
    price: 14190,
    color: 'Midnight',
    quantity: 70,
    screenSizeInch: 4.7,
    description: 'Small but mighty, featuring wireless charging and durable design.'
  },
  {
    id: 7,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'iPhone SE 2022',
    brandName: 'Apple',
    ramGb: 4,
    storageGb: 64,
    price: 14190,
    color: 'Starlight',
    quantity: 80,
    screenSizeInch: 4.7,
    description: 'Classic compact design perfect for one-handed use with modern performance.'
  },
  {
    id: 8,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'iPhone SE 2022',
    brandName: 'Apple',
    ramGb: 4,
    storageGb: 64,
    price: 14190,
    color: 'Blue',
    quantity: 90,
    screenSizeInch: 4.7,
    description: '5G ready with iOS latest updates and enhanced camera modes.'
  },
  {
    id: 9,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'iPhone SE 2022',
    brandName: 'Apple',
    ramGb: 4,
    storageGb: 64,
    price: 14190,
    color: 'White',
    quantity: 60,
    screenSizeInch: 4.7,
    description: 'Lightweight, stylish, and packed with power beyond its size.'
  }
]

const item = mockItems.find(item => item.id === parseInt(route.params.id))
</script>

<template>
    <Navbar />
    <div class="mt-10 flex items-center justify-center w-full">
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
      <!-- Profile Icon -->
      <div class="cursor-pointer">
        <i class="fas fa-user text-gray-600 text-2xl"></i>
      </div>
      <!-- Cart Icon -->
      <div class="cursor-pointer">
        <i class="fas fa-shopping-cart text-gray-600 text-2xl"></i>
      </div>
    </div>
  </div>
</div>

<div v-if="item" class="p-10 flex items-start space-x-10 relative mt-6">
  
  <div class="absolute top-0 left-0 w-full  text-gray-800 bg-white bg-opacity-70 p-2 ml-10 ">
    <p>
      <span class="text-blue-600">Home</span> &gt; 
      <span class="font-semibold">{{ item.model }}</span> /
      <span class="font-semibold">{{ item.storageGb }} GB</span> /
      <span class="font-semibold">{{ item.color }}</span>
    </p>
  </div>
  
  <div></div>
  <div class="w-1/3">
    <img :src="item.imageUrl" alt="image" class="w-100 h-auto rounded-lg mt-5" />
  </div>

  <div class="w-2/3 mt-10">
    <p class="text-lg mt-2">Brand: {{ item.brandName }}</p>
    <p class="text-lg text-blue-600 mt-2">Price: ฿{{ item.price }} Baht</p>
    <p class="text-lg mt-2">Description: <br> {{ item.description }} </p>
    <p class="text-lg mt-2">RAM: {{ item.ramGb }} GB</p>
    <p class="text-lg mt-2">Screen size: {{ item.screenSizeInch }} inches</p>
    <p class="text-lg mt-2">Storage: {{ item.storageGb }} GB</p>
    <p class="text-lg mt-2">Color: {{ item.color }}</p>
    <p class="text-lg mt-2">Available quantity: {{ item.quantity }} units</p>
  </div>
</div>

<div v-else class="p-10 flex justify-center">
  <div class="border border-red-500 p-6 rounded-md bg-red-50">
    <p class="text-red-600 text-xl mb-4">The requested sale item does not exist.</p>
    <div class="flex justify-center">
      <button 
        @click="goToHome" 
        class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">
        OK
      </button>
    </div>
  </div>
</div>



</template>
