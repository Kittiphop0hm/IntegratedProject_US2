<script setup>
import { computed, ref, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute()
const pathName = ref("")
const props = defineProps({
    roles: {
        type: Array,
        required: true
    },
})

watchEffect(() => {
    if (route.name === 'Register') {
        pathName.value = 'Register'
    } else if (route.name === 'Login') {
        pathName.value = 'Login'
    }
})
</script>

<template>
<div v-if="pathName === 'Register'" class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-6">
    <div class="bg-white rounded-2xl shadow-xl p-8 w-full max-w-md">
        <div class="text-center mb-8">
            <h2 class="text-3xl font-bold text-gray-800 mb-2">Create Account</h2>
            <p class="text-gray-600 text-[16px]">Welcome to ITB-MSHOP <span class="font-bold">US2</span></p>
        </div>
        
        <form class="space-y-6">
            <div class="space-y-2">
                <label for="nickname" class="block text-sm font-medium text-gray-700">Nickname</label>
                <input 
                    id="nickname"
                    type="text" 
                    placeholder="Enter your nickname"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
                >
            </div>
            
            <div class="space-y-2">
                <label for="fullname" class="block text-sm font-medium text-gray-700">Full Name</label>
                <input 
                    id="fullname"
                    type="text" 
                    placeholder="Enter your fullname"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
                >
            </div>
            
            <div class="space-y-2">
                <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
                <input 
                    id="email"
                    type="email" 
                    placeholder="Enter your email"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
                >
            </div>
            
            <div class="space-y-2 ">
                <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
                <input 
                    id="password"
                    type="password" 
                    placeholder="Enter your password"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 hover:border-gray-400 placeholder-gray-400"
                >
            </div>
            
            <div class="space-y-2">
                <label for="role" class="block text-sm font-medium text-gray-700">Role</label>
                <select 
                    id="role"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 bg-white cursor-pointer hover:border-gray-400"
                >
                    <option disabled selected class="text-gray-400">Select your role</option>
                    <option v-for="(role, index) in roles" :key="index" :value="role.value" class="text-gray-900">
                        {{ role.text }}
                    </option>
                </select>
            </div>
            
            <div class="w-full flex flex-row justify-center items-center space-y-2 pt-4 max-md:flex-col">
                <button 
                    type="submit"
                    class="w-full mx-1 flex-1 bg-blue-600 cursor-pointer hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition-colors duration-200 transform hover:scale-[1.02] active:scale-[0.98] shadow-lg"
                >
                    Submit
                </button>
                <router-link :to="{name: 'SaleItemHome'}" class="w-full text-center mx-1 flex-1 bg-gray-100 hover:bg-gray-200 text-gray-700 font-semibold py-3 px-6 rounded-lg transition-colors duration-200 transform hover:scale-[1.02] active:scale-[0.98] border border-gray-300">
                    <button 
                        type="button"
                        class="cursor-pointer"
                    >
                        Cancel
                    </button>
                </router-link>
            </div>
        </form>
        <div class="mt-8 pt-6 border-t border-gray-200 text-center">
            <p class="text-sm text-gray-600">
                Already have an account? 
                <router-link :to="{name: 'Login'}" href="#" class="text-blue-600 hover:text-blue-700 font-medium">Login</router-link>
            </p>
        </div>
    </div>
</div>


<div v-if="pathName === 'Login'" class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-6">
    <div class="bg-white rounded-2xl shadow-xl p-8 w-full max-w-md">
        <div class="text-center mb-8">
            <h2 class="text-3xl font-bold text-gray-800 mb-2">Create Account</h2>
            <p class="text-gray-600 text-[16px]">Welcome to ITB-MSHOP <span class="font-bold">US2</span></p>
        </div>
        
        <form class="space-y-6">
            <div class="space-y-2">
                <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
                <input 
                    id="email"
                    type="email" 
                    placeholder="Enter your email"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
                >
            </div>
            
            <div class="space-y-2 ">
                <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
                <input 
                    id="password"
                    type="password" 
                    placeholder="Enter your password"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 hover:border-gray-400 placeholder-gray-400"
                >
            </div>
            
            <div class="w-full flex flex-row justify-center items-center space-y-2 pt-4 max-md:flex-col">
                <button 
                    type="submit"
                    class="w-full mx-1 flex-1 bg-blue-600 cursor-pointer hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition-colors duration-200 transform hover:scale-[1.02] active:scale-[0.98] shadow-lg"
                >
                    Submit
                </button>
                <router-link :to="{name: 'SaleItemHome'}" class="w-full text-center mx-1 flex-1 bg-gray-100 hover:bg-gray-200 text-gray-700 font-semibold py-3 px-6 rounded-lg transition-colors duration-200 transform hover:scale-[1.02] active:scale-[0.98] border border-gray-300">
                    <button 
                        type="button"
                        class="cursor-pointer"
                    >
                        Cancel
                    </button>
                </router-link>
            </div>
        </form>
        <div class="mt-8 pt-6 border-t border-gray-200 text-center">
            <p class="text-sm text-gray-600">
                Don't have an account? 
                <router-link :to="{name: 'Register'}" href="#" class="text-blue-600 hover:text-blue-700 font-medium">Register</router-link>
            </p>
        </div>
    </div>
</div>
</template>

<style scoped>
</style>