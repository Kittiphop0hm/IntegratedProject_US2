<script setup>
import UserProfile from "@/components/user/UserProfile.vue";
import {onMounted, ref} from "vue";
import {addItem, editItem, getItemById, editItemWithToken} from "@/libs/fetchUtil.js";
import {decodeJWT} from "@/libs/decodeJWT.js";
import { useRoute, useRouter } from "vue-router";

const router = useRouter()
const accessToken = sessionStorage.getItem('accessToken')
const getUser = decodeJWT(accessToken)
const user = ref({})
const maskPhone = ref('')
const maskBankNo = ref('')

onMounted(async () => {
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
    const accessToken = sessionStorage.getItem("accessToken")
    console.log("old token:" + accessToken);
    const updateUser = await editItemWithToken(`${import.meta.env.VITE_APP_URL}/v2/users`, currentUser.id, currentUser, accessToken) 
    console.log("new token: " + updateUser.accessToken);
    const decodeNewToken = decodeJWT(updateUser.accessToken)
    console.log(decodeNewToken);
    sessionStorage.setItem("accessToken", updateUser.accessToken)
    sessionStorage.setItem("nickname", decodeNewToken.nickname)
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
</script>

<template>
  <UserProfile 
  :user="user" 
  :maskPhoneNumber="maskPhone"
  :maskBankNo="maskBankNo" 
  :isSuccess="isSuccess" 
  :isError="isError"
  @updateUser="editUser" />
</template>

<style scoped>

</style>