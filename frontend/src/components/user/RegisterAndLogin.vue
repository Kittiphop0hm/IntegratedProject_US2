<script setup>
import { computed, ref, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const emits = defineEmits(["register"])
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

const validateErrorEmail = ref("")
const validateEmail = (value) => {
    if (!value || value.includes("@")) {
        validateErrorEmail.value = ""
    } else {
        validateErrorEmail.value = "Email format is invalid."
    }
}

const validateErrorPassword = ref("")
const validatePassword = (value) => {
    const regex = /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%&*?.])[a-zA-Z0-9!@#$%&*?.]{8,50}$/
    if (!value || regex.test(value)) {
        validateErrorPassword.value = ""
    } else {
        validateErrorPassword.value = "Least 8 characters and at least one uppercase, lowercase, number, and special character."
    }
}

const validateErrorFullname = ref("")

const isShowPassword = ref(false)
const userFormat = ref({
    nickname: "",
    email: "",
    password: "",
    fullname: "",
    role: "",
    mobile: "",
    bankAccountNumber: "",
    nationalCardNumber: "",
    files: []
})
const isSeller = ref(false)
const inputForUserRole = () => userFormat.value.role === "seller" ? isSeller.value = true : isSeller.value = false
const enableSaveButton = computed(() => {
  return (
    userFormat.value.nickname.length > 0 &&
    userFormat.value.email.length > 0 &&
    userFormat.value.password.length > 0 &&
    userFormat.value.fullname.length > 0
  )
})

watchEffect(() => {
    console.log("nickname", userFormat.value.nickname);
    console.log("email", userFormat.value.email);
    console.log("password", userFormat.value.password);
    console.log("fullname", userFormat.value.fullname);
    console.log("role", userFormat.value.role);
    console.log("mobile", userFormat.value.mobile);
    console.log("bankAccountNumber", userFormat.value.bankAccountNumber);
    console.log("nationalCardNumber", userFormat.value.nationalCardNumber);
    console.log("files", userFormat.value.files);
    
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
            <div class="space-y-1">
                <label for="nickname" class="block text-sm font-medium text-gray-700">Nickname</label>
                <input 
                    v-model.trim="userFormat.nickname"
                    id="nickname"
                    type="text" 
                    placeholder="Enter your nickname"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
                >
            </div>

            <div class="space-y-1">
                <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
                <input
                    v-model.trim="userFormat.email" 
                    @blur="validateEmail(userFormat.email)"
                    id="email"
                    type="email" 
                    placeholder="Enter your email"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
                >
                <p class="text-red-500 text-[12px]">{{ validateErrorEmail }}</p>
            </div>

            <div class="space-y-1">
                <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
                <input 
                    v-model.trim="userFormat.password"
                    @blur="validatePassword(userFormat.password)"
                    id="password"
                    :type="isShowPassword ? 'text' : 'password'" 
                    placeholder="Enter your password"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 hover:border-gray-400 placeholder-gray-400"
                >
                <div class="h-full flex items-center my-1">
                    <input v-model.trim="isShowPassword" type="checkbox">
                    <label class="text-[12px] mx-1 text-center">Show password</label>
                </div>
                <p class="text-[12px] text-red-500">{{ validateErrorPassword }}</p>
            </div>
            
            <div class="space-y-1">
                <label for="fullname" class="block text-sm font-medium text-gray-700">Fullname</label>
                <input 
                    v-model.trim="userFormat.fullname"
                    id="fullname"
                    type="text" 
                    placeholder="Enter your fullname"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
                >
            </div>
            
            <div class="space-y-1">
                <label for="role" class="block text-sm font-medium text-gray-700">Roles</label>
                <select 
                    v-model.trim="userFormat.role"
                    @change="inputForUserRole"
                    id="role"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 bg-white cursor-pointer hover:border-gray-400"
                >
                    <option disabled selected value="" class="text-gray-400">Select your roles</option>
                    <option v-for="(role, index) in roles" :key="index" :value="role.value" class="text-gray-900">
                        {{ role.text }}
                    </option>
                </select>
            </div>
            
            <div v-if="isSeller" class="space-y-1">
                <label for="Mobile" class="block text-sm font-medium text-gray-700">Mobile</label>
                <input 
                    v-model.trim="userFormat.mobile"
                    id="Mobile"
                    type="text" 
                    placeholder="Enter your Mobile"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
                >
            </div>

            <div v-if="isSeller" class="space-y-1">
                <label for="Bank Account No" class="block text-sm font-medium text-gray-700">Bank Account No</label>
                <input 
                    v-model.trim="userFormat.bankAccountNumber"
                    id="Bank Account No"
                    type="number" 
                    placeholder="Enter your Bank Account No"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
                >
            </div>

            
            <div v-if="isSeller" class="space-y-1">
                <label for="National Card No" class="block text-sm font-medium text-gray-700">National Card No</label>
                <input 
                    v-model.trim="userFormat.nationalCardNumber"
                    id="National Card No"
                    type="number" 
                    placeholder="Enter your National Card No"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
                >
            </div>

            
          <div v-if="isSeller" class="mb-3 flex flex-col">
            <label class="block text-sm font-medium text-gray-700 mb-1">National Card Photo</label>
            <label
              for="fileFront"
              class="itbms-upload-button flex flex-col items-center justify-center w-full h-32 border-2 border-slate-300 border-dashed rounded-xl cursor-pointer bg-slate-50 hover:bg-slate-100 transition-all duration-200 group"
            >
              <div class="flex flex-col items-center justify-center pt-5 pb-6">
                <svg class="w-10 h-10 mb-3 text-slate-400 group-hover:text-slate-600 transition-colors duration-200" fill="none" stroke="currentColor" viewBox="0 0 48 48">
                  <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
                <p class="mb-2 text-sm text-slate-500 group-hover:text-slate-600">
                  <span class="font-semibold">Front side</span>
                </p>
              </div>
            </label>
            <input
              id="fileFront"
              type="file"
              multiple
              class="hidden"
              accept="image/*"
            />
          </div>

        <div v-if="isSeller" class="mb-3 flex flex-col">
            <label
              for="fileBack"
              class="itbms-upload-button flex flex-col items-center justify-center w-full h-32 border-2 border-slate-300 border-dashed rounded-xl cursor-pointer bg-slate-50 hover:bg-slate-100 transition-all duration-200 group"
            >
              <div class="flex flex-col items-center justify-center pt-5 pb-6">
                <svg class="w-10 h-10 mb-3 text-slate-400 group-hover:text-slate-600 transition-colors duration-200" fill="none" stroke="currentColor" viewBox="0 0 48 48">
                  <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
                <p class="mb-2 text-sm text-slate-500 group-hover:text-slate-600">
                  <span class="font-semibold">Back side</span>
                </p>
              </div>
            </label>
            <input
              id="fileBack"
              type="file"
              multiple
              class="hidden"
              accept="image/*"
            />
          </div>
            
            <div class="w-full flex flex-row justify-center items-center pt-4 max-md:flex-col">
                <button 
                    :disabled="!enableSaveButton"
                    @click="$emit('register', $event, userFormat)"
                    type="submit"
                    :class="enableSaveButton ? 'w-full mx-1 flex-1 bg-blue-600 cursor-pointer hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition-colors duration-200 transform hover:scale-[1.02] active:scale-[0.98] shadow-lg max-md:my-1' : 'w-full mx-1 flex-1 bg-gray-600 cursor-no-drop text-white font-semibold py-3 px-6 rounded-lg shadow-lg max-md:my-1'"
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
            <div class="space-y-1">
                <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
                <input 
                    id="email"
                    type="email" 
                    placeholder="Enter your email"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
                >
            </div>
            
            <div class="space-y-1">
                <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
                <input 
                    v-model.trim="userFormat.password"
                    @blur="validatePassword(userFormat.password)"
                    id="password"
                    :type="isShowPassword ? 'text' : 'password'" 
                    placeholder="Enter your password"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 hover:border-gray-400 placeholder-gray-400"
                >
                <div class="h-full flex items-center my-1">
                    <input v-model.trim="isShowPassword" type="checkbox">
                    <label class="text-[12px] mx-1 text-center">Show password</label>
                </div>
            </div>
            
            <div class="w-full flex flex-row justify-center items-center pt-4 max-md:flex-col">
                <button 
                    disabled=""
                    type="submit"
                    class="w-full mx-1 flex-1 bg-blue-600 cursor-pointer hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition-colors duration-200 transform hover:scale-[1.02] active:scale-[0.98] shadow-lg max-md:my-1"
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