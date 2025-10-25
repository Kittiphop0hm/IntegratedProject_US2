<script setup>
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { editForgotPassword } from '@/libs/fetchUtil';

const route = useRoute()
const router = useRouter()
const email = ref('')
const isClick = ref(false)
const newPassword = ref('')
const confirmPassword = ref('')
const showNewPassword = ref(false)
const showConfirmPassword = ref(false)
const isMatch = ref(false)


const sendEmail = async () => {
    if (email.value && email.value.length > 0) {
        isClick.value = !isClick.value
        localStorage.setItem('emailForChangePassword', email.value)
        const sendRequestForgotPassword = await fetch(`${import.meta.env.VITE_APP_URL}/v2/auth/request-forgot?email=${email.value}`, {
            method: 'POST'
        }) 
        console.log(sendRequestForgotPassword);
        console.log(email.value);
    } else {
        console.log("Email empty");   
    }
}

const sendNewPasswordConfirm = async () => {
    if (newPassword.value === confirmPassword.value) {
        isMatch.value = true
        const request = {
            email: localStorage.getItem('emailForChangePassword'),
            newPassword: newPassword.value
        }
        const changeNewPassword = await editForgotPassword(`${import.meta.env.VITE_APP_URL}/v2/auth/new-password`, request)
        console.log(changeNewPassword);
        if (changeNewPassword === 200) {
            localStorage.removeItem('emailForChangePassword')
            router.push({ name: 'SaleItemHome' })
        }
        
    } else {
        isMatch.value = false
        console.log("Not match");
    }
}

</script>

<template>
    <div class="w-full h-[100vh]">
        <div v-if="route.path === '/forgot-password'" class="w-full h-full flex justify-center items-center bg-linear-to-r from-cyan-500 to-blue-500">
            <div class="w-[30%] h-[30%] bg-white rounded-2xl">
                <div class="w-full h-full flex flex-col justify-center items-center px-6 gap-y-6">
                    <div class="w-full flex justify-center items-center">
                        <input v-model="email" type="text" placeholder="Enter your Email" class="w-full h-[50px] input text-sm" />
                    </div>
                    <div class="w-full flex justify-center items-center">
                        <button :disabled="isClick" @click="sendEmail" :class="!isClick ? 'w-full bg-linear-to-r from-cyan-500 to-blue-500 py-3 rounded-md text-white font-semibold cursor-pointer hover:opacity-80' : 'w-full bg-gray-400 py-3 rounded-md text-white font-semibold'">{{ !isClick ? 'Send' : 'Sent' }}</button>
                    </div>
                </div>
            </div>
        </div>

        <div v-if="route.path === '/new-password'" class="w-full h-full flex justify-center items-center bg-linear-to-r from-cyan-500 to-blue-500">
            <div class="w-[30%] h-[30%] bg-white rounded-2xl">
                <div class="w-full h-full flex flex-col justify-center items-center px-6 gap-y-6">
                    <div class="w-full flex flex-col justify-center items-center">
                        <input v-model="newPassword" :type="showNewPassword ? 'text' : 'password'" placeholder="New password" class="w-full h-[50px] input text-sm" />
                        <div class="w-full flex gap-x-2 my-1 text-left">
                            <input id="show1" v-model="showNewPassword" type="checkbox">
                            <label for="show1" class="text-[12px]">Show password</label>
                        </div>
                    </div>
                    <div class="w-full flex flex-col justify-center items-center">
                        <input v-model="confirmPassword" :type="showConfirmPassword ? 'text' : 'password'" placeholder="Confirm password" class="w-full h-[50px] input text-sm" />
                        <div class="w-full flex gap-x-2 my-1 text-left">
                            <input id="show2" v-model="showConfirmPassword" type="checkbox">
                            <label for="show2" class="text-[12px]">Show password</label>
                        </div>
                    </div>
                    <div class="w-full flex justify-center items-center">
                        <button @click="sendNewPasswordConfirm" class="w-full bg-linear-to-r from-cyan-500 to-blue-500 py-3 rounded-md text-white font-semibold cursor-pointer hover:opacity-80">Confirm</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>