<script setup>
import { onMounted, ref } from 'vue';
import { getItems } from '@/libs/fetchUtil';
import Navbar from '@/views/Navbar.vue';
import Search from '../Search.vue';
import BrandList from './BrandList.vue';

const brands = ref([])
onMounted(async () => {
    try {
        brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`);
        console.log(brands.value);
        
    } catch (error) {
        console.error('Error fetching brands:', error);
    }
})

</script>

<template>
    <Navbar/>
    <Search/>
    <div class="px-8">
        <button class="itbms-sale-item-add px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 cursor-pointer">Add Brand</button>
    </div>
    <div>
        <BrandList :brands="brands"/>
    </div>
</template>