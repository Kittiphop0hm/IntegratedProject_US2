<script setup>
import { ref } from 'vue';

const isApple = ref(true)
const isSamsung = ref(false)
const isXiaomi = ref(false)
const isHuawei = ref(false)
const isAsus = ref(false)
const isOppo = ref(false)


const IsAppleActive = () => {
    isApple.value = true
    isSamsung.value = false
    isXiaomi.value = false
    isHuawei.value = false
    isAsus.value = false
    isOppo.value = false
}
const IsSamsungActive = () => {
    isApple.value = false
    isSamsung.value = true
    isXiaomi.value = false
    isHuawei.value = false
    isAsus.value = false
    isOppo.value = false
}
const IsXiaomiActive = () => {
    isApple.value = false
    isSamsung.value = false
    isXiaomi.value = true
    isHuawei.value = false
    isAsus.value = false
    isOppo.value = false
}
const IsHuaweiActive = () => {
    isApple.value = false
    isSamsung.value = false
    isXiaomi.value = false
    isHuawei.value = true
    isAsus.value = false
    isOppo.value = false
}
const IsAsusActive = () => {
    isApple.value = false
    isSamsung.value = false
    isXiaomi.value = false
    isHuawei.value = false
    isAsus.value = true
    isOppo.value = false
}
const IsOppoActive = () => {
    isApple.value = false
    isSamsung.value = false
    isXiaomi.value = false
    isHuawei.value = false
    isAsus.value = false
    isOppo.value = true
}
</script>

<template>
    <div class="w-full max-h-full">
        <div class="w-full h-full flex flex-col justify-center items-center px-10 py-10 max-lg:px-5 max-lg:py-5">
            <div class="mb-5 w-full flex flex-col justify-center items-center">
                <h1 class="text-4xl font-bold">Special Offers</h1>
                <div class="w-[50%] my-5 font-semibold flex flex-row justify-around items-center max-lg:w-full max-[426px]:flex-col">
                    <button @click="IsAppleActive" :class="isApple ? 'border-b-2 cursor-pointer hover:opacity-70' : 'cursor-pointer hover:opacity-70'">Apple</button>
                    <button @click="IsSamsungActive" :class="isSamsung ? 'border-b-2 cursor-pointer hover:opacity-70' : 'cursor-pointer hover:opacity-70'">Samsung</button>
                    <button @click="IsXiaomiActive" :class="isXiaomi ? 'border-b-2 cursor-pointer hover:opacity-70' : 'cursor-pointer hover:opacity-70'">Xiaomi</button>
                    <button @click="IsHuaweiActive" :class="isHuawei ? 'border-b-2 cursor-pointer hover:opacity-70' : 'cursor-pointer hover:opacity-70'">Huawei</button>
                    <button @click="IsAsusActive" :class="isAsus ? 'border-b-2 cursor-pointer hover:opacity-70' : 'cursor-pointer hover:opacity-70'">ASUS</button>
                    <button @click="IsOppoActive" :class="isOppo ? 'border-b-2 cursor-pointer hover:opacity-70' : 'cursor-pointer hover:opacity-70'">OPPO</button>
                </div>
            </div>

            <!-- Apple -->
            <router-link to="/sale-items" v-show="isApple" class="w-full h-[60vh] flex flex-row justify-center items-center gap-5 max-lg:h-full max-lg:flex-col">
                <div to="/sale-items" class="w-[40%] h-full bg-gray-200 rounded-xl flex flex-col justify-around items-center p-10 cursor-pointer hover:shadow-xl max-lg:p-5 max-lg:w-full">
                    <div class="rounded-2xl w-full">
                        <div class="w-[10%] flex justify-center items-center bg-blue-500 rounded-2xl max-lg:w-[20%]">
                            <p class="text-center text-sm text-white">New</p>
                        </div>
                    </div>
                    <div>
                        <img src="/images/Image.png" alt="Iphone" class="w-[100%] h-[300px] rounded-xl shadow-2xl">
                    </div>
                    <div class="flex flex-col justify-center items-center mt-5">
                        <h1 class="text-center text-2xl font-bold">Iphone 14 Pro Max</h1>
                        <p>6/512 GB</p>
                    </div>
                </div>

                <div class="w-[40%] h-full grid grid-cols-2 gap-3 max-lg:w-full">
                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone 14-pro-1.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14</h1>
                                <p class="max-lg:text-sm">6/256 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone 14-pro-1.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14</h1>
                                <p class="max-lg:text-sm">6/256 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone-14-proMax.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14 Pro Max</h1>
                                <p class="max-lg:text-sm">6/512 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone-14-proMax.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14 Pro Max</h1>
                                <p class="max-lg:text-sm">6/512 GB</p>
                            </div>
                        </div>
                    </div>                   
                </div>
            </router-link>
            <!-- End apple -->

            <!-- Samsung -->
            <router-link to="/sale-items" v-show="isSamsung" class="w-full h-[60vh] flex flex-row justify-center items-center gap-5 max-lg:h-full max-lg:flex-col">
                <div class="w-[40%] h-full bg-gray-200 rounded-xl flex flex-col justify-around items-center p-10 cursor-pointer hover:shadow-xl max-lg:p-5 max-lg:w-full">
                    <div class="rounded-2xl w-full">
                        <div class="w-[10%] flex justify-center items-center bg-blue-500 rounded-2xl max-lg:w-[20%]">
                            <p class="text-center text-sm text-white">New</p>
                        </div>
                    </div>
                    <div>
                        <img src="/images/samsung.png" alt="Iphone" class="w-[100%] h-[100%]">
                    </div>
                    <div class="flex flex-col justify-center items-center mt-5">
                        <h1 class="text-center text-2xl font-bold">Samsung S23 Ultra</h1>
                        <p>-/512 GB</p>
                    </div>
                </div>

                <div class="w-[40%] h-full grid grid-cols-2 gap-3 max-lg:w-full">
                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone 14-pro-1.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14</h1>
                                <p class="max-lg:text-sm">6/256 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone 14-pro-1.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14</h1>
                                <p class="max-lg:text-sm">6/256 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone-14-proMax.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14 Pro Max</h1>
                                <p class="max-lg:text-sm">6/512 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone-14-proMax.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14 Pro Max</h1>
                                <p class="max-lg:text-sm">6/512 GB</p>
                            </div>
                        </div>
                    </div>                   
                </div>
            </router-link>
            <!-- End Samsung -->


            <!-- Xiaomi -->
            <router-link to="/sale-items" v-show="isXiaomi" class="w-full h-[60vh] flex flex-row justify-center items-center gap-5 max-lg:h-full max-lg:flex-col">
                <div class="w-[40%] h-full bg-gray-200 rounded-xl flex flex-col justify-around items-center p-10 cursor-pointer hover:shadow-xl max-lg:p-5 max-lg:w-full">
                    <div class="rounded-2xl w-full">
                        <div class="w-[10%] flex justify-center items-center bg-blue-500 rounded-2xl max-lg:w-[20%]">
                            <p class="text-center text-sm text-white">New</p>
                        </div>
                    </div>
                    <div>
                        <img src="/images/xiaomi.png" alt="Iphone" class="w-[100%] h-[400px]">
                    </div>
                    <div class="flex flex-col justify-center items-center mt-5">
                        <h1 class="text-center text-2xl font-bold">Xiaomi Note 12 Pro</h1>
                        <p>8/128 GB</p>
                    </div>
                </div>

                <div class="w-[40%] h-full grid grid-cols-2 gap-3 max-lg:w-full">
                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone 14-pro-1.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14</h1>
                                <p class="max-lg:text-sm">6/256 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone 14-pro-1.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14</h1>
                                <p class="max-lg:text-sm">6/256 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone-14-proMax.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14 Pro Max</h1>
                                <p class="max-lg:text-sm">6/512 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone-14-proMax.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14 Pro Max</h1>
                                <p class="max-lg:text-sm">6/512 GB</p>
                            </div>
                        </div>
                    </div>                   
                </div>
            </router-link>
            <!-- End Xiaomi -->

            <!-- Huawei -->
            <router-link to="/sale-items" v-show="isHuawei" class="w-full h-[60vh] flex flex-row justify-center items-center gap-5 max-lg:h-full max-lg:flex-col">
                <div class="w-[40%] h-full bg-gray-200 rounded-xl flex flex-col justify-around items-center p-10 cursor-pointer hover:shadow-xl max-lg:p-5 max-lg:w-full">
                    <div class="rounded-2xl w-full">
                        <div class="w-[10%] flex justify-center items-center bg-blue-500 rounded-2xl max-lg:w-[20%]">
                            <p class="text-center text-sm text-white">New</p>
                        </div>
                    </div>
                    <div>
                        <img src="/images/huawei.png" alt="Iphone" class="w-[100%] h-[400px]">
                    </div>
                    <div class="flex flex-col justify-center items-center mt-5">
                        <h1 class="text-center text-2xl font-bold">Huawei</h1>
                        <p>6/512 GB</p>
                    </div>
                </div>

                <div class="w-[40%] h-full grid grid-cols-2 gap-3 max-lg:w-full">
                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone 14-pro-1.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14</h1>
                                <p class="max-lg:text-sm">6/256 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone 14-pro-1.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14</h1>
                                <p class="max-lg:text-sm">6/256 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone-14-proMax.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14 Pro Max</h1>
                                <p class="max-lg:text-sm">6/512 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone-14-proMax.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14 Pro Max</h1>
                                <p class="max-lg:text-sm">6/512 GB</p>
                            </div>
                        </div>
                    </div>                   
                </div>
            </router-link>
            <!-- End Huawei -->


            <!-- ASUS -->
            <router-link to="/sale-items" v-show="isAsus" class="w-full h-[60vh] flex flex-row justify-center items-center gap-5 max-lg:h-full max-lg:flex-col">
                <div class="w-[40%] h-full bg-gray-200 rounded-xl flex flex-col justify-around items-center p-10 cursor-pointer hover:shadow-xl max-lg:p-5 max-lg:w-full">
                    <div class="rounded-2xl w-full">
                        <div class="w-[10%] flex justify-center items-center bg-blue-500 rounded-2xl max-lg:w-[20%]">
                            <p class="text-center text-sm text-white">New</p>
                        </div>
                    </div>
                    <div>
                        <img src="/images/asus.png" alt="Iphone" class="w-[100%] h-[300px]">
                    </div>
                    <div class="flex flex-col justify-center items-center mt-5">
                        <h1 class="text-center text-2xl font-bold">Asus</h1>
                        <p>6/512 GB</p>
                    </div>
                </div>

                <div class="w-[40%] h-full grid grid-cols-2 gap-3 max-lg:w-full">
                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone 14-pro-1.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14</h1>
                                <p class="max-lg:text-sm">6/256 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone 14-pro-1.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14</h1>
                                <p class="max-lg:text-sm">6/256 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone-14-proMax.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14 Pro Max</h1>
                                <p class="max-lg:text-sm">6/512 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone-14-proMax.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14 Pro Max</h1>
                                <p class="max-lg:text-sm">6/512 GB</p>
                            </div>
                        </div>
                    </div>                   
                </div>
            </router-link>
            <!-- End ASUS -->

            <!-- OPPO -->
            <router-link to="/sale-items" v-show="isOppo" class="w-full h-[60vh] flex flex-row justify-center items-center gap-5 max-lg:h-full max-lg:flex-col">
                <div class="w-[40%] h-full bg-gray-200 rounded-xl flex flex-col justify-around items-center p-10 cursor-pointer hover:shadow-xl max-lg:p-5 max-lg:w-full">
                    <div class="rounded-2xl w-full">
                        <div class="w-[10%] flex justify-center items-center bg-blue-500 rounded-2xl max-lg:w-[20%]">
                            <p class="text-center text-sm text-white">New</p>
                        </div>
                    </div>
                    <div>
                        <img src="/images/oppo.png" alt="Iphone" class="w-[100%] h-[400px]">
                    </div>
                    <div class="flex flex-col justify-center items-center mt-5">
                        <h1 class="text-center text-2xl font-bold">Oppo</h1>
                        <p>6/512 GB</p>
                    </div>
                </div>

                <div class="w-[40%] h-full grid grid-cols-2 gap-3 max-lg:w-full">
                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone 14-pro-1.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14</h1>
                                <p class="max-lg:text-sm">6/256 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone 14-pro-1.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14</h1>
                                <p class="max-lg:text-sm">6/256 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone-14-proMax.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14 Pro Max</h1>
                                <p class="max-lg:text-sm">6/512 GB</p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full bg-gray-200 rounded-xl flex flex-col justify-center items-center px-5 cursor-pointer hover:shadow-xl">
                        <div class="flex flex-col justify-around items-center">
                            <div class="my-2">
                                <img src="/images/Iphone-14-proMax.png" alt="">
                            </div>
                            <div class="my-2 flex flex-col justify-center items-center">
                                <h1 class="text-center text-xl font-bold max-lg:text-sm">Iphone 14 Pro Max</h1>
                                <p class="max-lg:text-sm">6/512 GB</p>
                            </div>
                        </div>
                    </div>                   
                </div>
            </router-link>
            <!-- End OPPO -->
        </div>
    </div>
    
</template>