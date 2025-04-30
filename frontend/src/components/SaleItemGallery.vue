<script setup>
import { RouterLink } from 'vue-router'
const mockItems = [
  {
    id: 1,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'iPhone 14 Pro Max',
    brandName: 'Apple',
    storageGb: 512,
    price: 42900
  },
  {
    id: 2,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'Galaxy S23 Ultra',
    brandName: 'Samsung',
    storageGb: 512,
    price: 32900
  },
  {
    id: 3,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'Xiaomi 13',
    brandName: 'Xiaomi',
    storageGb: 256,
    price: 22900
  },
  {
    id: 4,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'iPhone SE 2022',
    brandName: 'Apple',
    storageGb: 64,
    price: 14190
  },
  {
    id: 5,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'OnePlus 11',
    brandName: 'OnePlus',
    storageGb: 256,
    price: 25900
  },
  {
    id: 6,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'iPhone SE 2022',
    brandName: 'Apple',
    storageGb: 64,
    price: 14190
  },
  {
    id: 7,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'iPhone SE 2022',
    brandName: 'Apple',
    storageGb: 64,
    price: 14190
  },
  {
    id: 8,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'iPhone SE 2022',
    brandName: 'Apple',
    storageGb: 64,
    price: 14190
  },
  {
    id: 9,
    imageUrl: 'https://th.bing.com/th/id/OIP.CgJkA9EfxBeI7iFKGi3t-AHaJN?pid=ImgDet&w=185&h=230&c=7&dpr=1.3',
    model: 'iPhone SE 2022',
    brandName: 'Apple',
    storageGb: 64,
    price: 14190
  },
]
</script>

<template>
<div class="p-10 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-6">
  <div
    v-for="item in mockItems"
    :key="item.id"
    class="border rounded-xl shadow hover:shadow-md p-4 transition duration-300 ease-in-out cursor-pointer Itbms-row"
  >
    <router-link :to="{ name: 'SaleItemDetail', params: { id: item.id } }" class="block">
      <img :src="item.imageUrl" alt="phone image" class="w-full h-36 object-cover rounded-md mb-4" />

      <div class="text-sm text-gray-900 Itbms-brand">
        {{ item.brandName }}
      </div>

      <div class="text-lg font-semibold text-gray-800 Itbms-model">
        {{ item.model }}
      </div>

      <div class="text-sm mt-1 Itbms-storageGb">
        {{ item.storageGb }}<span class="Itbms-storageGb-unit"> GB</span>
      </div>

      <div class="text-blue-600 text-lg font-bold mt-2 Itbms-price">
        Baht:{{ item.price }}<span class="Itbms-price-unit"></span>
      </div>
    </router-link>
  </div>
</div>
</template>
