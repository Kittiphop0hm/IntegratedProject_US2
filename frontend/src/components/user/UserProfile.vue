<script setup>
import { ref, watchEffect, computed } from 'vue';
import {useRoute} from "vue-router";

const emits = defineEmits(['updateUser', 'changePassword', 'update:showChangePasswordModal'])
const props = defineProps({
  user: {
    type: Object,
    required: true
  },
  isSuccess: {
    type: Boolean,
    default: false
  },
  isError: {
    type: Boolean,
    default: false
  },
  maskPhoneNumber: {
    type: String,
    required: false
  },
  maskBankNo: {
    type: String,
    required: false
  },
  isChangePasswordSuccess: {
    type: Boolean,
    default: false
  },
  isChangePasswordError: {
    type: Boolean,
    default: false
  },
  changePasswordErrorMessage: {
    type: String,
    default: ''
  },
  showChangePasswordModal: {
    type: Boolean,
    default: false
  }
})

const route = useRoute()
const currentUser = ref({})
const oldUser = ref({})
watchEffect(() => {
  props.user ? currentUser.value = {...props.user} : currentUser.value = {}
  props.user ? oldUser.value = {...props.user} : oldUser.value = {}
})

const validateErrorFullname = ref("");
const validateFullname = (value) => {
  if (value.length >= 4 && value.length <= 40) {
    validateErrorFullname.value = "";
  } else {
    validateErrorFullname.value =
      "Fullname must be between 4 and 40 characters long";
  }
};

const validateErrorNickname = ref("");
const validateNickname = (value) => {
  if (value.length >= 3 && value.length <= 40) {
    validateErrorNickname.value = "";
  } else {
    validateErrorNickname.value =
      "Nickname must be between 3 and 40 characters long";
  }
};

const enableEditBtn = computed(() => {
  const validateError = validateErrorFullname.value.length <= 0 && validateErrorNickname.value.length <= 0
  return validateError && JSON.stringify(currentUser.value) !== JSON.stringify(oldUser.value)
})

// Change Password Validation
const passwordData = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})
const showCurrentPassword = ref(false)
const showNewPassword = ref(false)
const showConfirmPassword = ref(false)

// Error messages 
const passwordErrors = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const openChangePasswordModal = () => {
  emits('update:showChangePasswordModal', true)
  passwordData.value = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
  passwordErrors.value = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
}

const closeChangePasswordModal = () => {
  emits('update:showChangePasswordModal', false)
  passwordData.value = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
  passwordErrors.value = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
}

// Validate New Password 
const validateNewPassword = (password) => {
  passwordErrors.value.newPassword = ''
  
  if (!password) {
    return
  }
  
  if (password.length < 8) {
    passwordErrors.value.newPassword = 'Password must be at least 8 characters'
    return
  }
  
  if (!/[A-Z]/.test(password)) {
    passwordErrors.value.newPassword = 'Password must contain at least one uppercase letter'
    return
  }
  
  if (!/[a-z]/.test(password)) {
    passwordErrors.value.newPassword = 'Password must contain at least one lowercase letter'
    return
  }
  
  if (!/\d/.test(password)) {
    passwordErrors.value.newPassword = 'Password must contain at least one number'
    return
  }
  
  if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>/?]/.test(password)) {
    passwordErrors.value.newPassword = 'Password must contain at least one special character'
    return
  }
}

// Validate Confirm Password
const validateConfirmPassword = () => {
  passwordErrors.value.confirmPassword = ''
  
  if (!passwordData.value.confirmPassword) {
    return
  }
  
  if (passwordData.value.newPassword !== passwordData.value.confirmPassword) {
    passwordErrors.value.confirmPassword = 'New password and confirm password do not match'
  }
}

// 
const handleChangePassword = () => {
  // Reset errors
  passwordErrors.value = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
  
  if (!passwordData.value.currentPassword) {
    passwordErrors.value.currentPassword = 'Current password is required'
    return
  }
  
  if (!passwordData.value.newPassword) {
    passwordErrors.value.newPassword = 'New password is required'
    return
  }
  
  if (!passwordData.value.confirmPassword) {
    passwordErrors.value.confirmPassword = 'Confirm password is required'
    return
  }
  
  validateNewPassword(passwordData.value.newPassword)
  if (passwordErrors.value.newPassword) {
    return
  }
  
  validateConfirmPassword()
  if (passwordErrors.value.confirmPassword) {
    return
  }
  
  emits('changePassword', passwordData.value)
}

const canSubmitPassword = computed(() => {
  return passwordData.value.currentPassword && 
         passwordData.value.newPassword && 
         passwordData.value.confirmPassword &&
         !passwordErrors.value.newPassword &&
         !passwordErrors.value.confirmPassword
})

watchEffect(() => {
  if (props.changePasswordErrorMessage) {
    if (props.changePasswordErrorMessage.includes('Current password is incorrect')) {
      passwordErrors.value.currentPassword = 'Current password is incorrect'
    } else if (props.changePasswordErrorMessage.includes('New password must be different from current password')) {
      passwordErrors.value.newPassword = 'New password must be different from current password'
    }
  }
})
</script>

<template>
  <div class="w-full h-[110vh] px-10 py-5 relative max-lg:h-full max-[450px]:p-0">
    <div class="absolute top-2 right-0 z-20">

      <div v-if="isSuccess" role="alert" class="alert alert-success rounded-r-none opacity-90">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 shrink-0 stroke-current" fill="none" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <span>Profile data is updated successfully.</span>
      </div>

      <div v-if="isError" role="alert" class="alert alert-error rounded-r-none opacity-90">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 shrink-0 stroke-current" fill="none" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <span>Error! Profile data can't updated.</span>
      </div>

   
      <div v-if="isChangePasswordSuccess" role="alert" class="alert alert-success rounded-r-none opacity-90">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 shrink-0 stroke-current" fill="none" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <span>Password changed successfully!</span>
      </div>

      
      <div v-if="isChangePasswordError && !passwordErrors.currentPassword && !passwordErrors.newPassword" 
           role="alert" class="alert alert-error rounded-r-none opacity-90">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 shrink-0 stroke-current" fill="none" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <span>{{ changePasswordErrorMessage }}</span>
      </div>
    </div>

    <div class="w-full h-full flex items-center justify-between rounded-2xl shadow-2xl">
      <div class="w-full h-full bg-white p-10 rounded-2xl max-[450px]:p-5">
        <div class="w-full h-hull">
          <div class="text-2xl my-5">
            <div class="breadcrumbs text-sm">
              <ul>
                <li><router-link :to="{name: 'SaleItemList'}" class="itbms-home">Home</router-link></li>
                <li><router-link :to="{name: 'UserProfile'}" class="font-semibold">Profile</router-link></li>
              </ul>
            </div>
            <h1>Welcome, <span class="font-medium">{{ oldUser.nickName }}</span></h1>
          </div>

          <div class="w-full h-[100px] bg-linear-to-r from-[#523F31] to-[#B88D6E] rounded-2xl"></div>

          <div class="flex items-center mt-5 max-lg:flex-col max-lg:justify-center max-lg:text-center">
            <div class="avatar avatar-placeholder">
              <div class="bg-[#2D1E17] text-neutral-content w-24 rounded-full">
                <span class="text-3xl">{{ oldUser.fullName?.charAt(0) }}</span>
              </div>
            </div>

            <div class="w-full flex justify-between items-center mx-5 max-lg:flex-col">
              <div>
                <p class="font-semibold">{{ oldUser.fullName }}</p>
                <p>{{ oldUser.email }}</p>
              </div>

              <div v-if="route.path === '/profile'" class="flex gap-3 max-lg:mt-2">
                <button 
                  @click="openChangePasswordModal"
                  class="itbms-change-password-button bg-[#796254] py-2 px-6 rounded-lg cursor-pointer hover:opacity-80 text-white font-semibold">
                  Change Password
                </button>
                <router-link :to="{name: 'UserProfileEdit'}">
                  <button class="itbms-profile-button bg-[#796254] py-2 px-6 rounded-lg cursor-pointer hover:opacity-80 text-white font-semibold">Edit</button>
                </router-link>
              </div>

              <div v-if="route.path === '/profile/edit'" class="flex gap-3 max-lg:mt-2">
                <button
                @click="$emit('updateUser', currentUser)" 
                :disabled="!enableEditBtn"
                :class="enableEditBtn ? 'itbms-save-button bg-[#796254] py-2 px-6 rounded-lg cursor-pointer hover:opacity-80 text-white font-semibold' : 'itbms-save-button bg-gray-600 py-2 px-6 rounded-lg hover:opacity-80 text-white font-semibold cursor-not-allowed'">
                Save
                </button>
                <router-link :to="{name: 'UserProfile'}">
                  <button class="itbms-cancel-button bg-gray-400 py-2 px-6 rounded-lg cursor-pointer hover:opacity-80 text-white font-semibold">Cancel</button>
                </router-link>
              </div>
            </div>
          </div>

          <div class="w-full h-full flex justify-center items-center">
            <div class="w-full h-full grid grid-cols-2 gap-x-20 gap-y-10 mt-10 max-lg:grid-cols-1">

              <div v-if="route.path === '/profile'">
                <h1 class="font-semibold">Fullname</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p class="itbms-fullname max-[450px]:text-sm">{{ currentUser?.fullName }}</p>
                </div>
              </div>

              <div v-if="route.path === '/profile/edit'">
                <h1 class="font-semibold">Fullname</h1>
                <div>
                  <input v-model.trim="currentUser.fullName" @change="validateFullname(currentUser.fullName)" type="text" class="itbms-fullname w-full border-1 border-gray-300 p-3 rounded-lg max-[450px]:text-sm">
                </div>
                <p class="text-red-400 text-sm">{{ validateErrorFullname }}</p>
              </div>

              <div v-if="route.path === '/profile'">
                <h1 class="font-semibold">Nickname</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p class="itbms-nickname max-[450px]:text-sm">{{ currentUser?.nickName }}</p>
                </div>
              </div>

              <div v-if="route.path === '/profile/edit'">
                <h1 class="font-semibold">Nickname</h1>
                <div>
                  <input v-model.trim="currentUser.nickName" @change="validateNickname(currentUser.nickName)" type="text" class="itbms-nickname w-full border-1 border-gray-300 p-3 rounded-lg max-[450px]:text-sm">
                </div>
                <p class="text-red-400 text-sm">{{ validateErrorNickname }}</p>
              </div>

              <div v-if="route.path === '/profile'">
                <h1 class="font-semibold">Email</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p class="itbms-email max-[450px]:text-sm">{{ currentUser?.email }}</p>
                </div>
              </div>

              <div v-if="route.path === '/profile/edit'">
                <h1 class="font-semibold">Email</h1>
                <div>
                  <input disabled v-model.trim="currentUser.email" type="text" class="itbms-email w-full bg-gray-200 p-3 rounded-lg max-[450px]:text-sm">
                </div>
              </div>

              <div v-if="currentUser?.phoneNumber && route.path === '/profile'">
                <h1 class="font-semibold">Phone</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p class="itbms-mobile max-[450px]:text-sm">{{ maskPhoneNumber }}</p>
                </div>
              </div>

              <div v-if="route.path === '/profile/edit'">
                <h1 class="font-semibold">Phone</h1>
                <div>
                  <input disabled :value="maskPhoneNumber" type="text" class="itbms-mobile w-full bg-gray-200 p-3 rounded-lg max-[450px]:text-sm">
                </div>
              </div>

              <div v-if="currentUser?.bankAccount && route.path === '/profile'">
                <h1 class="font-semibold">Bankaccount</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p class="itbms-bankAccount max-[450px]:text-sm">{{ maskBankNo }}</p>
                </div>
              </div>

              <div v-if="route.path === '/profile/edit'">
                <h1 class="font-semibold">Bankaccount</h1>
                <div>
                  <input disabled :value="maskBankNo" type="text" class="itbms-bankAccount w-full bg-gray-200 p-3 rounded-lg max-[450px]:text-sm">
                </div>
              </div>

              <div v-if="currentUser?.bankName && route.path === '/profile'">
                <h1 class="font-semibold">Bankname</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p class="itbms-bankName max-[450px]:text-sm">{{ currentUser?.bankName }}</p>
                </div>
              </div>

              <div v-if="route.path === '/profile/edit'">
                <h1 class="font-semibold">Bankname</h1>
                <div>
                  <input disabled :value="currentUser?.bankName" type="text" class="itbms-bankName w-full bg-gray-200 p-3 rounded-lg max-[450px]:text-sm">
                </div>
              </div>

              <div v-if="currentUser?.userType">
                <h1 class="font-semibold">User type</h1>
                <div class="flex justify-between items-center bg-gray-200 p-3 rounded-lg">
                  <p class="itbms-type max-[450px]:text-sm">{{ currentUser?.userType === 'BUYER' ? 'Buyer' : 'Seller'}}</p>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>

    <!--  Modal Change Password with Real-time Validation -->
    <div v-if="showChangePasswordModal" 
         class="fixed inset-0 z-50 flex items-center justify-center"
         @click.self="closeChangePasswordModal">
      <div class="absolute inset-0 bg-white bg-opacity-50 backdrop-blur-sm"></div>
      
      <div class="relative bg-white rounded-2xl shadow-2xl p-8 w-full max-w-md mx-4 z-10">
        <div class="flex justify-between items-center mb-6">
          <h2 class="text-2xl font-bold text-gray-800">Change Password</h2>
          <button 
            @click="closeChangePasswordModal" 
            class="text-gray-400 hover:text-gray-600 text-3xl font-bold leading-none">
            ×
          </button>
        </div>

        <div class="space-y-4">
          <!-- Current Password -->
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-2">Current Password</label>
            <div class="relative">
              <input 
                v-model="passwordData.currentPassword"
                :type="showCurrentPassword ? 'text' : 'password'"
                :class="passwordErrors.currentPassword ? 'border-red-500' : 'border-gray-300'"
                class="w-full border rounded-lg p-3 pr-10 focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Enter current password">
              <button 
                type="button"
                @click="showCurrentPassword = !showCurrentPassword"
                class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 hover:text-gray-700">
                <svg v-if="showCurrentPassword" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                  <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
                  <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd" />
                </svg>
                <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M3.707 2.293a1 1 0 00-1.414 1.414l14 14a1 1 0 001.414-1.414l-1.473-1.473A10.014 10.014 0 0019.542 10C18.268 5.943 14.478 3 10 3a9.958 9.958 0 00-4.512 1.074l-1.78-1.781zm4.261 4.26l1.514 1.515a2.003 2.003 0 012.45 2.45l1.514 1.514a4 4 0 00-5.478-5.478z" clip-rule="evenodd" />
                  <path d="M12.454 16.697L9.75 13.992a4 4 0 01-3.742-3.741L2.335 6.578A9.98 9.98 0 00.458 10c1.274 4.057 5.065 7 9.542 7 .847 0 1.669-.105 2.454-.303z" />
                </svg>
              </button>
            </div>
            <p v-if="passwordErrors.currentPassword" class="text-red-500 text-sm mt-1">
              {{ passwordErrors.currentPassword }}
            </p>
          </div>

          <!-- New Password -->
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-2">New Password</label>
            <div class="relative">
              <input 
                v-model="passwordData.newPassword"
                @input="validateNewPassword(passwordData.newPassword)"
                :type="showNewPassword ? 'text' : 'password'"
                :class="passwordErrors.newPassword ? 'border-red-500' : 'border-gray-300'"
                class="w-full border rounded-lg p-3 pr-10 focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Enter new password">
              <button 
                type="button"
                @click="showNewPassword = !showNewPassword"
                class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 hover:text-gray-700">
                <svg v-if="showNewPassword" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                  <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
                  <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd" />
                </svg>
                <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M3.707 2.293a1 1 0 00-1.414 1.414l14 14a1 1 0 001.414-1.414l-1.473-1.473A10.014 10.014 0 0019.542 10C18.268 5.943 14.478 3 10 3a9.958 9.958 0 00-4.512 1.074l-1.78-1.781zm4.261 4.26l1.514 1.515a2.003 2.003 0 012.45 2.45l1.514 1.514a4 4 0 00-5.478-5.478z" clip-rule="evenodd" />
                  <path d="M12.454 16.697L9.75 13.992a4 4 0 01-3.742-3.741L2.335 6.578A9.98 9.98 0 00.458 10c1.274 4.057 5.065 7 9.542 7 .847 0 1.669-.105 2.454-.303z" />
                </svg>
              </button>
            </div>
            <p v-if="passwordErrors.newPassword" class="text-red-500 text-sm mt-1">
              {{ passwordErrors.newPassword }}
            </p>
            <p v-else class="text-gray-500 text-xs mt-1">
              Must be 8+ chars with uppercase, lowercase, number & special char
            </p>
          </div>

          <!-- Confirm Password -->
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-2">Confirm New Password</label>
            <div class="relative">
              <input 
                v-model="passwordData.confirmPassword"
                @input="validateConfirmPassword"
                :type="showConfirmPassword ? 'text' : 'password'"
                :class="passwordErrors.confirmPassword ? 'border-red-500' : 'border-gray-300'"
                class="w-full border rounded-lg p-3 pr-10 focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Confirm new password">
              <button 
                type="button"
                @click="showConfirmPassword = !showConfirmPassword"
                class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 hover:text-gray-700">
                <svg v-if="showConfirmPassword" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                  <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
                  <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd" />
                </svg>
                <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M3.707 2.293a1 1 0 00-1.414 1.414l14 14a1 1 0 001.414-1.414l-1.473-1.473A10.014 10.014 0 0019.542 10C18.268 5.943 14.478 3 10 3a9.958 9.958 0 00-4.512 1.074l-1.78-1.781zm4.261 4.26l1.514 1.515a2.003 2.003 0 012.45 2.45l1.514 1.514a4 4 0 00-5.478-5.478z" clip-rule="evenodd" />
                  <path d="M12.454 16.697L9.75 13.992a4 4 0 01-3.742-3.741L2.335 6.578A9.98 9.98 0 00.458 10c1.274 4.057 5.065 7 9.542 7 .847 0 1.669-.105 2.454-.303z" />
                </svg>
              </button>
            </div>
            <p v-if="passwordErrors.confirmPassword" class="text-red-500 text-sm mt-1">
              {{ passwordErrors.confirmPassword }}
            </p>
          </div>
        </div>

        <!-- Buttons -->
        <div class="flex gap-3 mt-6">
          <button 
            @click="handleChangePassword"
            :disabled="!canSubmitPassword"
            :class="canSubmitPassword 
              ? 'bg-amber-900 hover:opacity-80 cursor-pointer' 
              : 'bg-gray-400 cursor-not-allowed'"
            class="flex-1 text-white font-semibold py-3 rounded-lg transition-opacity">
            Change Password
          </button>
          <button 
            @click="closeChangePasswordModal"
            class="flex-1 bg-gray-300 hover:bg-gray-400 text-gray-800 font-semibold py-3 rounded-lg transition-colors">
            Cancel
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
</style>