<script setup>
import Navbar from '@/views/Navbar.vue';
import Search from '../Search.vue';
import { computed, ref } from 'vue';
import { addItem } from '@/libs/fetchUtil';
import { useRouter } from 'vue-router';

const router = useRouter()
const brand = ref({
    name:''.trim(),
    websiteUrl:''.trim(),
    countryOfOrigin:''.trim(),
    isActive:false
})

const enableButton = computed(() => {
    return (
        brand.value.name !== ''
    )
})

const addBrand = async () => {
    if (enableButton) { 
        try {
            const addBrandResponse = await addItem(`${import.meta.env.VITE_APP_URL}/v1/brands`, brand.value)
            if (addBrandResponse.status === 201 || addBrandResponse.status === 200) {
                    router.push({ path:'/brands', query: { alertAdd: "true" } });
            } 
            if (addBrandResponse.status === 400 || addBrandResponse.status === 500) {
                    router.push({ path:'/brands', query: { alertErrors: "true" } });
            }
        } catch (error) {
            console.error('Error adding brand:', error);
        }
    }
}
</script>

<template>
    <Navbar/>
    <Search/>
    <div class="px-2 py-10 w-full max-h-[100vh]">
        <div class="w-full h-[80vh] flex flex-col items-center justify-center">
            <div class="w-full flex justify-around items-center py-5">
                <div>
                    <p>
                    <router-link to="/sale-items/list" class="itbms-item-list text-blue-500 hover:text-blue-300">Sale item List</router-link>
                    >
                    <router-link to="/brands" class="itbms-manage-brand text-blue-500 hover:text-blue-300">Brand List</router-link>
                    > 
                    <span class="font-bold">Add Brand</span>
                    </p>
                </div>
                <div>
                </div>
            </div>

            <div class="w-full h-full flex justify-center">
            <div class="w-[50%] h-[80%] max-w-lg p-6 bg-white border border-gray-300 rounded-2xl shadow-md max-md:w-full">
                <form @submit.prevent="addBrand" class="flex flex-col justify-center gap-4">
                <h2 class="text-2xl font-semibold text-center mb-4">Add New Brand</h2>
                <div class="flex flex-col">
                    <label for="brandName" class="text-sm font-medium mb-1">Brand Name:</label>
                    <input 
                    v-model.trim="brand.name"
                    id="brandName" 
                    type="text" 
                    placeholder="Enter brand name" 
                    class="itbms-name input w-full max-w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring focus:ring-blue-300" 
                    />
                </div>

                <div class="flex flex-col">
                    <label for="websiteUrl" class="text-sm font-medium mb-1">Website URL:</label>
                    <input 
                    v-model.trim="brand.websiteUrl"
                    id="websiteUrl" 
                    type="text" 
                    placeholder="Enter website URL" 
                    class="itbms-websiteUrl input w-full max-w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring focus:ring-blue-300" 
                    />
                </div>

                <div class="flex flex-col">
                    <label for="isActive" class="text-sm font-medium mb-1">Is Active:</label>
                    <input v-model.trim="brand.isActive" type="checkbox" checked="checked" class="itbms-isActive toggle" />
                </div>

                <div class="flex flex-col">
                    <label for="country" class="text-sm font-medium mb-1">Country of Origin:</label>
                    <input 
                    v-model.trim="brand.countryOfOrigin"
                    id="country" 
                    type="text" 
                    placeholder="Enter country of origin" 
                    class="itbms-countryOfOrigin input w-full max-w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring focus:ring-blue-300" 
                    />
                </div>

                <div class="flex justify-center mt-6">
                    <button 
                    type="submit"
                    :disabled="!enableButton" 
                    :class="!enableButton ? 'itbms-save-button px-6 py-2 mr-2 bg-gray-400 text-white rounded-lg' : 'itbms-save-button cursor-pointer px-6 py-2 mr-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 focus:outline-none focus:ring focus:ring-blue-300'"
                    >
                    Save
                    </button>
                    <router-link :to="{ name: BrandManager }">
                        <button 
                        type="button" 
                        class="itbms-cancel-button cursor-pointer px-6 py-2 ml-2 bg-gray-300 text-gray-700 rounded-lg hover:bg-gray-400 focus:outline-none focus:ring focus:ring-gray-300"
                        >
                        Cancel
                        </button>
                    </router-link>
                </div>
                </form>
            </div>
        </div>
        </div>
    </div>
</template>