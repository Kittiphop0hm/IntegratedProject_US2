<script setup>
import { useRoute, useRouter } from 'vue-router';
import RegisterAndLogin from './RegisterAndLogin.vue';
import { onMounted, ref } from 'vue';
import { register } from '@/libs/fetchUtil';

const roles = ref([
    {
    text: 'Buyer',
    value: 'buyer'
    },
    {
    text: 'Seller',
    value: 'seller'
    },
])

const registerForm = (event, user) => {
    event.preventDefault()
    console.log(user);
    
    
    console.log("Submit"); 
}

</script>

<template>
<RegisterAndLogin :roles="roles" @register="registerForm"></RegisterAndLogin>

</template>

<style scoped>

</style>