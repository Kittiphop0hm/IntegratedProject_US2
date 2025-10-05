<script setup>
import { useRoute } from 'vue-router';
import { ref } from 'vue';
import Navbar from '@/views/Navbar.vue';
import Search from '../Search.vue';


const route = useRoute()
console.log(route.params.orderId);
const orderId = ref(route.params.orderId) 


</script>

<template>
    <div>
        <Navbar />
        <Search />
        <div class="w-full h-full p-5 space-y-3">
            <div class="w-full h-full bg-gray-200 rounded-2xl p-5">
                <div class="breadcrumbs text-sm">
                    <ul>
                        <li><a>Home</a></li>
                        <li><a>Documents</a></li>
                        <li>Add Document</li>
                    </ul>
                </div>
                <div class="w-full h-[50px] flex items-center">
                    
                </div>
            </div>
        </div>
    </div>
</template>