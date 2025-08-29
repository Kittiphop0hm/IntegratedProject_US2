<script setup>
import Navbar from '@/views/Navbar.vue';
import { onMounted , ref } from 'vue';
import { useRoute } from 'vue-router';  // <-- ต้อง import
import { verifyEmail } from "../../libs/fetchUtil.js";
const route = useRoute(); 
const result = ref();
onMounted(async () => {
    
  try {
    const token = route.query.token
    result.value = await verifyEmail(
      `${import.meta.env.VITE_APP_URL}/v2/users/verify-email?token=${token}`
    );
    console.log(result.value.status);
    console.log("Email verification result:", result.value);
  } catch (err) {
    console.error("Error fetching sale items:", err);
  }
});
</script>

<template>
    <Navbar />
    <div v-if="!result">
        <div class="flex flex-col items-center justify-center min-h-screen bg-gray-700">
            <div class="bg-gray-300 p-8 rounded shadow-md text-center text-gray-700">
                <h1 class="text-2xl font-bold mb-4">Verifying your email...</h1>
                <p class="mb-6">Please wait while we verify your email address.</p>
                <div class="loader ease-linear rounded-full border-8 border-t-8 border-gray-600 h-16 w-16 mx-auto"></div>
         </div>
         </div>
    </div>
    <div v-else-if="result.status === 200">
        <div class="flex flex-col items-center justify-center min-h-screen bg-gray-700">
            <div class="bg-white p-8 rounded shadow-md text-center">
                <h1 class="text-2xl font-bold mb-4 text-green-600">Email Verified Successfully!</h1>
                <p class="text-gray-700 mb-6">Your account has been successfully activated</p>
                <router-link :to="{ name: 'SaleItemHome' }" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Go to Login</router-link>
            </div>
        </div>
    </div>
    <div v-else>
                <div class="flex flex-col items-center justify-center min-h-screen bg-gray-700">
            <div class="bg-white p-8 rounded shadow-md text-center">
                <h1 class="text-2xl font-bold mb-4 text-red-600">Email Verification Failed</h1>
                <p class="mb-6 text-gray-700">An error occured or the verification link has expired. Please request a new verification email.</p>
                <router-link :to="{ name: 'SaleItemHome' }" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Go to SaleItem</router-link>
            </div>
        </div>
    </div>
</template>


<style scoped>
</style>