<script setup>
import { useRoute, useRouter } from "vue-router";
import RegisterAndLogin from "./RegisterAndLogin.vue";
import { onMounted, ref } from "vue";
import { register, addItem } from "@/libs/fetchUtil";


const saveTokens = (accessToken, refreshToken, nickname) => {
  sessionStorage.setItem('accessToken', accessToken);
  sessionStorage.setItem('refreshToken', refreshToken);
  sessionStorage.setItem('nickname', nickname);
};

// ฟังก์ชันสำหรับ decode JWT token
const decodeJWT = (token) => {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error('Error decoding JWT:', error);
    return null;
  }
};

const router = useRouter();
const roles = ref([
  {
    text: "Buyer",
    value: "BUYER",
  },
  {
    text: "Seller",
    value: "SELLER",
  },
]);

const is401 = ref(false);
const is400 = ref(false);
const is403 = ref(false); 

const registerForm = async (event, user) => {
  event.preventDefault();
  console.log(user);
  console.log("Submit");
  console.log(user.files);
  try {
    const registerUser = await register(
      `${import.meta.env.VITE_APP_URL}/v2/users/register`,
      user,
      user.files
    );
    console.log(registerUser.status);
    console.log(registerUser.error);
    if (registerUser.status === 201) {
      router.push({ name: "SaleItemHome", query: { alertAddUser: "true" } });
    }
  } catch (err) {
    console.log(err);
  }
};

const loginUser = ref();
const loginForm = async (event, user) => {
  console.log(user);
  console.log("loginForm");
  event.preventDefault();
  
  try {
    loginUser.value = await addItem(
      `${import.meta.env.VITE_APP_URL}/v2/users/authentications`,
      user
    );
    console.log(loginUser.value.status);
    
    if (loginUser.value.status === 400) {
      is400.value = true;
      is401.value = false;
      is403.value = false;
    }

    if (loginUser.value.status === 401) {
      is401.value = true;
      is400.value = false;
      is403.value = false;
    }

    if (loginUser.value.status === 403) {
      is403.value = true;
      is400.value = false;
      is401.value = false;
    }

    
    if (loginUser.value.status === 200) {
      let responseData = loginUser.value.data || loginUser.value;
      const accessToken = responseData.access_token;
      const refreshToken = responseData.refresh_token;
      
      let nickName = responseData.nickName; 
      if (!nickName && accessToken) {
        const decodedToken = decodeJWT(accessToken);
        if (decodedToken) {
          nickName = decodedToken.nickname;
          console.log('Decoded JWT payload:', decodedToken);
        }
      }
      
      console.log('Extracted values:', { accessToken, refreshToken, nickName });
      
      if (accessToken && refreshToken && nickName) {
        saveTokens(accessToken, refreshToken, nickName);
        router.push({ 
          name: "SaleItemHome",
          query: { loginSuccess: "true" }
        });
      }
    }

  } catch (err) {
    console.log(err);
  }
};
</script>

<template>
  <RegisterAndLogin
    :roles="roles"
    :is401="is401"
    :is400="is400"
    :is403="is403"
    @register="registerForm"
    @login="loginForm"
  ></RegisterAndLogin>
</template>

<style scoped></style>