<script setup>
import { useRoute, useRouter } from 'vue-router';
import RegisterAndLogin from './RegisterAndLogin.vue';
import { onMounted, ref } from 'vue';

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

</script>

<template>
<RegisterAndLogin :roles="roles"></RegisterAndLogin>

</template>

<style scoped>

</style>