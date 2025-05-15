<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getItemById,editItem } from '@/libs/fetchUtil';
import Navbar from '@/views/Navbar.vue';
import Search from '../Search.vue';

const {params: {id}} = useRoute()
const router = useRouter()
const selectBrand = ref({})
const editObjectBrand = ref({})
const enableButton = computed(() => {
    return (
        selectBrand.value.name !== '' &&
        selectBrand.value.websiteUrl !== '' &&  
        selectBrand.value.isActive !== '' &&
        selectBrand.value.countryOfOrigin !== '' &&
        selectBrand.value.name !== editObjectBrand.value.name ||
        selectBrand.value.websiteUrl !== editObjectBrand.value.websiteUrl ||
        selectBrand.value.isActive !== editObjectBrand.value.isActive ||
        selectBrand.value.countryOfOrigin !== editObjectBrand.value.countryOfOrigin
    )
})
onMounted(async () => {
    try {
        selectBrand.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v1/brands`, id)
        editObjectBrand.value = {...selectBrand.value}
    } catch (error) {
        console.error('Error fetching brand:', error);
    }
})

const editBrand = async () => {
    if (enableButton) {
        try {
            const editBrandResponse = await editItem(`${import.meta.env.VITE_APP_URL}/v1/brands`, selectBrand.value.id, selectBrand.value)
            if (editBrandResponse.status === 200) {
                router.push({ path:'/brands', query: { alertBrandEdit: "true" } });
            } else {
                router.push({ path:'/brands', query: { alertBrandEditError: "false" } });
            }
        } catch(error) {
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
                    <router-link to="/brands" class="text-blue-500">Home</router-link>
                    > Edit Brand
                    </p>
                </div>
                <div>
                </div>
            </div>

            <div class="w-full h-full flex justify-center">
            <div class="w-[50%] h-[80%] max-w-lg p-6 bg-white border border-gray-300 rounded-2xl shadow-md">
                <form @submit.prevent="editBrand" class="flex flex-col justify-center gap-4">
                <h2 class="text-2xl font-semibold text-center mb-4">Add New Brand</h2>
                <div class="flex flex-col">
                    <label for="brandName" class="text-sm font-medium mb-1">Brand Name:</label>
                    <input 
                    v-model="selectBrand.name"
                    id="brandName" 
                    type="text" 
                    placeholder="Enter brand name" 
                    class="input w-full max-w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring focus:ring-blue-300" 
                    />
                </div>

                <div class="flex flex-col">
                    <label for="websiteUrl" class="text-sm font-medium mb-1">Website URL:</label>
                    <input 
                    v-model="selectBrand.websiteUrl"
                    id="websiteUrl" 
                    type="text" 
                    placeholder="Enter website URL" 
                    class="input w-full max-w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring focus:ring-blue-300" 
                    />
                </div>

                <div class="flex flex-col">
                    <label for="isActive" class="text-sm font-medium mb-1">Is Active:</label>
                    <select 
                    v-model="selectBrand.isActive"
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
                    v-model="selectBrand.countryOfOrigin"
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