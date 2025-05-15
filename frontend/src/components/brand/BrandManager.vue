<script setup>
import { onMounted, ref } from 'vue';
import { getItems } from '@/libs/fetchUtil';
import Navbar from '@/views/Navbar.vue';
import Search from '../Search.vue';
import BrandList from './BrandList.vue';
import { useRoute } from 'vue-router';

const brands = ref([])
const route = useRoute();
onMounted(async () => {
    try {
        brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`);
    } catch (error) {
        console.error('Error fetching brands:', error);
    }
})
</script>

<template>
    <Navbar/>
    <Search/>
    <div v-show="route.query.alertBrandAdd || route.query.alertBrandAddError" class="p-10 pb-0 mb-10">
        <div class="bg-black/5 shadow-xl rounded px-8 pt-6 pb-8">
            <div v-show="route.query.alertBrandAdd">
                <h1 class=" text-2xl text-green-400">Successfully</h1>
                <br>
                <p class="itbms-message">The brand has been added</p>
            </div>
            <div v-show="route.query.alertBrandAddError">
                <h1 class=" text-2xl text-red-400">Error</h1>
                <br>
                <p class="itbms-message">The status could not be added.</p>
            </div>
        </div>
    </div>

    <div v-show="route.query.alertBrandEdit || route.query.alertBrandEditError" class="p-10 pb-0 mb-10">
        <div class="bg-black/5 shadow-xl rounded px-8 pt-6 pb-8">
            <div v-show="route.query.alertBrandEdit">
                <h1 class=" text-2xl text-green-400">Successfully</h1>
                <br>
                <p class="itbms-message">The brand has been updated</p>
            </div>
            <div v-show="route.query.alertBrandEditError">
                <h1 class=" text-2xl text-red-400">Error</h1>
                <br>
                <p class="itbms-message">The brand does not exist.</p>
            </div>
        </div>
    </div>

    <div class="px-8 flex items-center">
        <router-link to="/sale-items">
            <button class="itbms-item-list text-blue-400">Sale Item List </button>
        </router-link>

        <p class="px-1 font-bold text-gray-400">></p>

        <router-link to="/brands/add">
            <button class="itbms-add-button px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 cursor-pointer">Add Brand</button>
        </router-link>
    </div>
    <div>
        <BrandList :brands="brands"/>
    </div>
</template>