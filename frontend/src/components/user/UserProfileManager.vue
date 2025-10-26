<script setup>
import UserProfile from "@/components/user/UserProfile.vue";
import {onMounted, ref} from "vue";
import {addItem, editItem, getItemById} from "@/libs/fetchUtil.js";
import {decodeJWT} from "@/libs/decodeJWT.js";
import { useRoute, useRouter } from "vue-router";

const router = useRouter()
const accessToken = localStorage.getItem('accessToken')
const getUser = decodeJWT(accessToken)
const user = ref({})
const maskPhone = ref('')
const maskBankNo = ref('')

onMounted(async () => {
  if(!accessToken)router.push({name:'Login'})
  try {
    const data = await getItemById(`${import.meta.env.VITE_APP_URL}/v2/users`, getUser.id)
    user.value = data
    maskPhone.value = maskNumber(user.value.phoneNumber)
    maskBankNo.value = maskNumber(user.value.bankAccount)
  } catch (error) {
    console.error("Error fetching user data:", error);
  }
})

const maskNumber = (number) => {
  if (!number) return '';

  const len = number.length;
  if (len < 4) {
    return 'x'.repeat(len);
  }

  const second = number.charAt(len - 2);
  const third  = number.charAt(len - 3);
  const fourth = number.charAt(len - 4);

  const head = 'x'.repeat(len - 4 > 0 ? len - 4 : 0);

  return `${head}${fourth}${third}${second}x`;
}

const isSuccess = ref(false)
const isError = ref(false)
const editUser = async (currentUser) => {
  if (currentUser) {
    const updateUser = await editItem(`${import.meta.env.VITE_APP_URL}/v2/users`, currentUser.id, currentUser) 
    console.log(updateUser);
    localStorage.setItem("nickname", updateUser.nickName)
     
    router.push({name: 'UserProfile'})
    if (updateUser.status === 200) {
        user.value = updateUser
        isSuccess.value = true
        setTimeout(() => {
          isSuccess.value = false
        }, 5000); 
    } else {
        isError.value = true
        setTimeout(() => {
          isError.value = false
        }, 5000);
    }
  }
}


const isChangePasswordSuccess = ref(false)
const isChangePasswordError = ref(false)
const changePasswordErrorMessage = ref('')
const showChangePasswordModal = ref(false)

const handleChangePassword = async (passwordData) => {
  try {
    const response = await fetch(`${import.meta.env.VITE_APP_URL}/v2/users/${getUser.id}/change-password`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`
      },
      credentials: 'include',
      body: JSON.stringify(passwordData)
    })

    if (response.ok || response.status === 200) {
      
      isChangePasswordSuccess.value = true
      isChangePasswordError.value = false
      changePasswordErrorMessage.value = ''
      showChangePasswordModal.value = false // ปิด modal
      
      setTimeout(() => {
        isChangePasswordSuccess.value = false
      }, 5000)
    } else {
     
      const errorData = await response.json()
      isChangePasswordError.value = true
      isChangePasswordSuccess.value = false
      
      changePasswordErrorMessage.value = errorData.message || 'Failed to change password'
      
      setTimeout(() => {
        isChangePasswordError.value = false
        changePasswordErrorMessage.value = ''
      }, 5000)
    }
  } catch (error) {
    console.error('Error changing password:', error)
    isChangePasswordError.value = true
    isChangePasswordSuccess.value = false
    changePasswordErrorMessage.value = 'Network error. Please try again.'
    
    setTimeout(() => {
      isChangePasswordError.value = false
      changePasswordErrorMessage.value = ''
    }, 5000)
  }
}
</script>

<template>
  <UserProfile 
    :user="user" 
    :maskPhoneNumber="maskPhone"
    :maskBankNo="maskBankNo" 
    :isSuccess="isSuccess" 
    :isError="isError"
    :isChangePasswordSuccess="isChangePasswordSuccess"
    :isChangePasswordError="isChangePasswordError"
    :changePasswordErrorMessage="changePasswordErrorMessage"
    v-model:showChangePasswordModal="showChangePasswordModal"
    @updateUser="editUser"
    @changePassword="handleChangePassword" />
</template>

<style scoped>
</style>