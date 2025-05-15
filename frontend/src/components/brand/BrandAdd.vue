<script setup>
import Navbar from '@/views/Navbar.vue';
import Search from '../Search.vue';
import { computed, ref } from 'vue';
import { addItem } from '@/libs/fetchUtil';
import { useRouter } from 'vue-router';

const router = useRouter()
const brand = ref({
    name:'',
    websiteUrl:'',
    countryOfOrigin:'',
    isActive:true
})

const enableButton = computed(() => {
    return (
        brand.value.name !== '' &&
        brand.value.websiteUrl !== '' &&
        brand.value.isActive !== '' &&
        brand.value.countryOfOrigin !== ''
    )
})

const addBrand = async () => {
    if (enableButton) { 
        try {
            const addBrandResponse = await addItem(`${import.meta.env.VITE_APP_URL}/v1/brands`, brand.value)
            if (addBrandResponse.status === 201 || addBrandResponse.status === 200) {
                    router.push({ path:'/brands', query: { alertBrandAdd: "true" } });
            } else {
                    router.push({ path:'/brands', query: { alertBrandAddError: "false" } });
            }
        } catch (error) {
            console.error('Error adding brand:', error);
        }
    }
}

console.log(enableButton.value);


</script>

<template>
    <Navbar/>
    <Search/>
    <div class="px-2 py-10 w-full max-h-[100vh]">
        <div class="w-full h-[80vh] flex flex-col items-center justify-center">
            <div class="w-full flex justify-around items-center py-5">
                <div>
                    <p>
                    <router-link to="/brands" class="text-blue-500">Home</router-link>
                    > Add Brand
                    </p>
                </div>
                <div>
                </div>
            </div>

            <div class="w-full h-full flex justify-center">
            <div class="w-[50%] h-[80%] max-w-lg p-6 bg-white border border-gray-300 rounded-2xl shadow-md">
                <form @submit.prevent="addBrand" class="flex flex-col justify-center gap-4">
                <h2 class="text-2xl font-semibold text-center mb-4">Add New Brand</h2>
                <div class="flex flex-col">
                    <label for="brandName" class="text-sm font-medium mb-1">Brand Name:</label>
                    <input 
                    v-model="brand.name"
                    id="brandName" 
                    type="text" 
                    placeholder="Enter brand name" 
                    class="input w-full max-w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring focus:ring-blue-300" 
                    />
                </div>

                <div class="flex flex-col">
                    <label for="websiteUrl" class="text-sm font-medium mb-1">Website URL:</label>
                    <input 
                    v-model="brand.websiteUrl"
                    id="websiteUrl" 
                    type="text" 
                    placeholder="Enter website URL" 
                    class="input w-full max-w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring focus:ring-blue-300" 
                    />
                </div>

                <div class="flex flex-col">
                    <label for="isActive" class="text-sm font-medium mb-1">Is Active:</label>
                    <select 
                    v-model="brand.isActive"
                    id="isActive" 
                    class="w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring focus:ring-blue-300"
                    >
                    <option value="true">Active</option>
                    <option value="false">Not Active</option>
                    </select>
                </div>

                <div class="flex flex-col">
                    <label for="country" class="text-sm font-medium mb-1">Country of Origin:</label>
                    <input 
                    v-model="brand.countryOfOrigin"
                    id="country" 
                    type="text" 
                    placeholder="Enter country of origin" 
                    class="input w-full max-w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring focus:ring-blue-300" 
                    />
                </div>

                <div class="flex justify-center mt-6">
                    <button 
                    type="submit" 
                    :class="!enableButton ? 'px-6 py-2 mr-2 bg-gray-400 text-white rounded-lg hover:bg-gray-500 focus:outline-none' : 'cursor-pointer px-6 py-2 mr-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 focus:outline-none focus:ring focus:ring-blue-300'"
                    >
                    Save
                    </button>
                    <router-link to="/brands">
                        <button 
                        type="button" 
                        class="cursor-pointer px-6 py-2 ml-2 bg-gray-300 text-gray-700 rounded-lg hover:bg-gray-400 focus:outline-none focus:ring focus:ring-gray-300"
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