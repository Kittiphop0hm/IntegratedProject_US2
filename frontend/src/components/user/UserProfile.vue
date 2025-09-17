<script setup>
import { ref, watchEffect, onMounted, computed } from 'vue';
import {useRoute} from "vue-router";

const emits = defineEmits(['updateUser'])
const props = defineProps({
  user: {
    type: Object,
    required: true
  },
  userPictureCard: {
    type: Array,
    required: false
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
</script>

<template>
  <div class="w-full h-[110vh] px-10 py-5 relative max-lg:h-full max-[450px]:p-0">
    <div class="absolute top-2 right-0">

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
    </div>

    <div class="w-full h-full flex items-center justify-between rounded-2xl shadow-2xl">
      <div class="w-full h-full bg-white p-10 rounded-2xl max-[450px]:p-5">
        <div class="w-full h-hull">
          <div class="text-2xl my-5">
            <h1>Welcome, <span class="font-medium">{{ oldUser.nickName }}</span></h1>
          </div>

          <div class="w-full h-[100px] bg-linear-to-r from-cyan-500 to-blue-500 rounded-2xl"></div>

          <div class="flex items-center mt-5 max-lg:flex-col max-lg:justify-center max-lg:text-center">
            <div class="avatar avatar-placeholder">
              <div class="bg-neutral text-neutral-content w-24 rounded-full">
                <span class="text-3xl">{{ oldUser.fullName?.charAt(0) }}</span>
              </div>
            </div>

            <div class="w-full flex justify-between items-center mx-5 max-lg:flex-col">
              <div>
                <p class="font-semibold">{{ oldUser.fullName }}</p>
                <p>{{ oldUser.email }}</p>
              </div>

              <div v-if="route.path === '/profile'">
                <router-link :to="{name: 'UserProfileEdit'}">
                  <button class="bg-linear-to-r from-blue-400 to-blue-600 py-2 px-6 rounded-lg cursor-pointer hover:opacity-80 text-white font-semibold max-lg:mt-2">Edit</button>
                </router-link>
              </div>
              <div v-if="route.path === '/profile/edit'" class="flex gap-3 max-lg:mt-2">
                <button
                @click="$emit('updateUser', currentUser)" 
                :disabled="!enableEditBtn"
                :class="enableEditBtn ? 'bg-linear-to-r from-blue-400 to-blue-600 py-2 px-6 rounded-lg cursor-pointer hover:opacity-80 text-white font-semibold' : 'bg-gray-600 py-2 px-6 rounded-lg hover:opacity-80 text-white font-semibold cursor-not-allowed'">Save</button>
                <router-link :to="{name: 'UserProfile'}">
                  <button class="bg-gray-400 py-2 px-6 rounded-lg cursor-pointer hover:opacity-80 text-white font-semibold">Cancel</button>
                </router-link>
              </div>
            </div>
          </div>

          <div class="w-full h-full flex justify-center items-center">
            <div class="w-full h-full grid grid-cols-2 gap-x-20 gap-y-10 mt-10 max-lg:grid-cols-1">

              <div v-if="route.path === '/profile'">
                <h1 class="font-semibold">Fullname</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p class="max-[450px]:text-sm">{{ currentUser?.fullName }}</p>
                </div>
              </div>

              <div v-if="route.path === '/profile/edit'">
                <h1 class="font-semibold">Fullname</h1>
                <div>
                  <input v-model="currentUser.fullName" @change="validateFullname(currentUser.fullName)" type="text" class="w-full border-1 border-gray-300 p-3 rounded-lg max-[450px]:text-sm">
                </div>
                <p class="text-red-400 text-sm">{{ validateErrorFullname }}</p>
              </div>

              <div v-if="route.path === '/profile'">
                <h1 class="font-semibold">Nickname</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p class="max-[450px]:text-sm">{{ currentUser?.nickName }}</p>
                </div>
              </div>

              <div v-if="route.path === '/profile/edit'">
                <h1 class="font-semibold">Nickname</h1>
                <div>
                  <input v-model="currentUser.nickName" @change="validateNickname(currentUser.nickName)" type="text" class="w-full border-1 border-gray-300 p-3 rounded-lg max-[450px]:text-sm">
                </div>
                <p class="text-red-400 text-sm">{{ validateErrorNickname }}</p>
              </div>

              <div>
                <h1 class="font-semibold">Email</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p class="max-[450px]:text-sm">{{ currentUser?.email }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Password</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p class="max-[450px]:text-sm">xxx</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Phonenumber</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p class="max-[450px]:text-sm">{{ maskPhoneNumber }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Bankaccount</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p class="max-[450px]:text-sm">{{ currentUser?.bankAccount ? currentUser?.bankAccount : "-" }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Bankname</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p class="max-[450px]:text-sm">{{ currentUser?.bankName ? currentUser?.bankName : "-" }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">User type</h1>
                <div class="flex justify-between items-center bg-gray-200 p-3 rounded-lg">
                  <p class="max-[450px]:text-sm">{{ currentUser?.userType}}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">National ID pictures</h1>
                <div class="flex flex-col justify-center bg-gray-200 p-3 rounded-lg gap-y-2">
                  <p class="max-[450px]:text-sm"><span class="font-semibold">Front:</span> {{ userPictureCard[0]?.filename }}</p>
                  <p class="max-[450px]:text-sm"><span class="font-semibold">Back:</span> {{ userPictureCard[1]?.filename }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
</style>