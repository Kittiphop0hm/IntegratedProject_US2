<script setup>
import Navbar from '@/views/Navbar.vue';
import Search from '../Search.vue';
import { computed, onMounted, ref, watchEffect } from 'vue';
import { addItem } from '@/libs/fetchUtil';
import { useRoute, useRouter } from 'vue-router';
import { getItemById, editItem } from '@/libs/fetchUtil';
import ValidationInput from '../validation/ValidationInput.vue';

const router = useRouter()
const {params: {id}} = useRoute()
const isEdit = ref(false)
const isAdd = ref(false)
const brand = ref({
    name:'',
    websiteUrl:'',
    countryOfOrigin:'',
    isActive:true
})
const editObjectBrand = ref({})

onMounted(async () => {
    try {
        if (id) {
            const fetchBrand = await getItemById(`${import.meta.env.VITE_APP_URL}/v1/brands`, id)
            editObjectBrand.value = {...fetchBrand}
            isEdit.value = true
            brand.value = {
                name: fetchBrand.name,
                websiteUrl: fetchBrand.websiteUrl,
                countryOfOrigin: fetchBrand.countryOfOrigin,
                isActive: fetchBrand.isActive
            }  
        } else {
            isAdd.value = true
        }
    } catch (error) {
        console.error('Error fetching brand:', error);
    }
})

const validateBrandNameMassage = ref("")
const validateBrandUrlMassage = ref("")
const validateBrandCountryMassage = ref("")

const validateBrandName = () => {
    if (brand.value.name.length >= 1 && brand.value.name.length <= 30) {
        validateBrandNameMassage.value = ''
    } else {
        validateBrandNameMassage.value = 'Brand name must be 1-30 characters long.'
    }
}

const validateBrandUrl = () => {
    if (!brand.value.websiteUrl || brand.value.websiteUrl.toLowerCase().includes('www') || brand.value.websiteUrl.toLowerCase().includes('http') || brand.value.websiteUrl.toLowerCase().includes('https')) {
        validateBrandUrlMassage.value = ''
    } else if(!brand.value.countryOfOrigin) {
        validateBrandUrlMassage.value = 'Brand URL must be a valid URL or not specified.'
    }
}


const validateBrandCountry = () => {
    if (!brand.value.countryOfOrigin || brand.value.countryOfOrigin.length >= 1 && brand.value.countryOfOrigin.length <= 80) {
        validateBrandCountryMassage.value = ''
    } else {
        validateBrandCountryMassage.value = 'Brand country of origin must be 1-80 characters long or not specified.'
    }
}

// const checkValidateSubmitForm = () => {
//     if (brand.value.name.length < 1 || brand.value.name.length > 30) {
//         validateBrandNameMassage.value = 'Brand name must be 1-30 characters long.'
//     } 
//     if (!brand.value.websiteUrl.toLowerCase().includes('www')) {
//         validateBrandUrlMassage.value = 'Brand URL must be a valid URL or not specified.'
//     } 
//     if (brand.value.countryOfOrigin.length < 1 || brand.value.countryOfOrigin.length > 80) {
//         validateBrandCountryMassage.value = 'Brand country of origin must be 1-80 characters long or not specified.'
//     }
// }

// const validateInputMassage = (brandNameMassage, brandUrlMassage, brandCountryMassage) => {
//     console.log(brandNameMassage);
//     console.log(brandUrlMassage);
//     console.log(brandCountryMassage);
//     validateBrandNameMassage.value = brandNameMassage
//     validateBrandUrlMassage.value = brandUrlMassage
//     validateBrandCountryMassage.value = brandCountryMassage
// }

const enableAddButton = computed(() => {
    const noEmptyFields = 
        brand.value.name !== '' 
    const noValidateInputMassage = 
        validateBrandNameMassage.value.length === 0 &&
        validateBrandCountryMassage.value.length === 0 &&
        validateBrandUrlMassage.value.length === 0
    return noEmptyFields && noValidateInputMassage
})

const enableEditButton = computed(() => {
    const noEmptyFields = 
        brand.value.name !== ''
    const isModify =   
        brand.value.name !== editObjectBrand.value.name ||
        brand.value.websiteUrl !== editObjectBrand.value.websiteUrl ||
        brand.value.isActive !== editObjectBrand.value.isActive ||
        brand.value.countryOfOrigin !== editObjectBrand.value.countryOfOrigin
    const noValidateInputMassage = 
        validateBrandNameMassage.value.length === 0 &&
        validateBrandCountryMassage.value.length === 0 &&
        validateBrandUrlMassage.value.length === 0
    return noEmptyFields && noValidateInputMassage && isModify
})

const addBrand = async () => {
    // checkValidateSubmitForm()
    if (enableAddButton.value) { 
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

const editBrand = async () => {
    // checkValidateSubmitForm()
    if (enableEditButton.value) {
        try {
            const editBrandResponse = await editItem(`${import.meta.env.VITE_APP_URL}/v1/brands`, editObjectBrand.value.id, brand.value)
            if (editBrandResponse.status === 200) {
                router.push({ path:'/brands', query: { alertUpdate: "true" } });
            } else {
                router.push({ path:'/brands', query: { alert404: "true" } });
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
    <!-- <ValidationInput :brand-name="brand.name" :brand-url="brand.websiteUrl" :brand-country="brand.countryOfOrigin" @massage-validate="validateInputMassage"></ValidationInput> -->
        <div class="px-2 py-10 w-full max-h-[100vh]">
            <div class="w-full h-[80vh] flex flex-col items-center justify-center">
                <div class="w-full flex justify-around items-center py-5">
                    <div>
                        <p>
                        <router-link to="/sale-items/list" class="itbms-item-list text-blue-500 hover:text-blue-300">Sale item List</router-link>
                        >
                        <router-link to="/brands" class="itbms-manage-brand text-blue-500 hover:text-blue-300">Brand List</router-link>
                        > 
                        <span class="font-bold">{{ isAdd ? "Add Brand" : "Edit Brand" }}</span>
                        </p>
                    </div>
                    <div>
                    </div>
                </div>

                <div class="w-full h-full flex justify-center">
                    <div class="w-[50%] h-[80%] max-w-lg p-6 bg-white border border-gray-300 rounded-2xl shadow-md max-md:w-full">
                        <form @submit.prevent="!isAdd ? editBrand() : addBrand()" class="flex flex-col justify-center gap-4">
                        <h2 class="text-2xl font-semibold text-center mb-4">Add New Brand</h2>
                        <div class="flex flex-col">
                            <label for="brandName" class="text-sm font-medium mb-1">Brand Name:</label>
                            <input 
                            v-model.trim="brand.name"
                            id="brandName" 
                            @blur="validateBrandName"
                
                            type="text" 
                            placeholder="Enter brand name" 
                            class="itbms-name input w-full max-w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring focus:ring-blue-300" 
                            />
                            <p class="text-red-500 itbms-message" v-if="validateBrandNameMassage">{{ validateBrandNameMassage }}</p>
                        </div>

                        <div class="flex flex-col">
                            <label for="websiteUrl" class="text-sm font-medium mb-1">Website URL:</label>
                            <input 
                            v-model.trim="brand.websiteUrl"
                            @blur="validateBrandUrl"
                            
                            id="websiteUrl" 
                            type="text" 
                            placeholder="Enter website URL" 
                            class="itbms-websiteUrl input w-full max-w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring focus:ring-blue-300" 
                            />
                            <p class="text-red-500 itbms-message" v-if="validateBrandUrlMassage">{{ validateBrandUrlMassage }}</p>
                        </div>

                        <div class="flex flex-col">
                            <label for="isActive" class="text-sm font-medium mb-1">Is Active:</label>
                            <input v-model.trim="brand.isActive" type="checkbox" checked="checked" class="itbms-isActive toggle" />
                        </div>

                        <div class="flex flex-col">
                            <label for="country" class="text-sm font-medium mb-1">Country of Origin:</label>
                            <input 
                            v-model.trim="brand.countryOfOrigin"
                            @blur="validateBrandCountry"
                            
                            id="country" 
                            type="text" 
                            placeholder="Enter country of origin" 
                            class="itbms-countryOfOrigin input w-full max-w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring focus:ring-blue-300" 
                            />
                            <p class="text-red-500 itbms-message" v-if="validateBrandCountryMassage">{{ validateBrandCountryMassage }}</p>
                        </div>

                        <div class="flex justify-center mt-6">
                            <button 
                                v-show="isAdd"
                                type="submit"
                                :disabled="!enableAddButton" 
                                :class="!enableAddButton ? 'itbms-save-button px-6 py-2 mr-2 bg-gray-400 text-white rounded-lg' : 'itbms-save-button cursor-pointer px-6 py-2 mr-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 focus:outline-none focus:ring focus:ring-blue-300'"
                                >
                                Save
                            </button>
                            <button 
                                v-show="isEdit"
                                type="submit"
                                :disabled="!enableEditButton" 
                                :class="!enableEditButton ? 'itbms-save-button px-6 py-2 mr-2 bg-gray-400 text-white rounded-lg' : 'itbms-save-button cursor-pointer px-6 py-2 mr-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 focus:outline-none focus:ring focus:ring-blue-300'"
                                >
                                Edit
                            </button>
                            <router-link :to="{ name: 'BrandManager' }">
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