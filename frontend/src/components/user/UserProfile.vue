<script setup>
import { computed, ref } from 'vue';
import {useRoute} from "vue-router";
const props = defineProps({
  user: {
    type: Object,
    required: true
  }
})
const route = useRoute()
const currentUser = computed(() => props.user)
console.log(route.path)
</script>

<template>
  <div class="w-full h-[100vh] px-10 py-5">
    <div class="w-full h-full flex items-center justify-between rounded-2xl shadow-2xl">
      <div class="w-full h-full bg-white p-10 rounded-2xl">
        <div class="w-full h-hull">
          <div class="text-2xl my-5">
            <h1>Welcome, <span class="font-medium">{{ currentUser.nickName }}</span></h1>
          </div>

          <div class="w-full h-[100px] bg-linear-to-r from-cyan-500 to-blue-500 rounded-2xl"></div>

          <div class="flex items-center mt-5">
            <div class="avatar avatar-placeholder">
              <div class="bg-neutral text-neutral-content w-24 rounded-full">
                <span class="text-3xl">{{ currentUser.fullName?.charAt(0) }}</span>
              </div>
            </div>

            <div class="w-full flex justify-between items-center mx-5">
              <div>
                <p class="font-semibold">{{ currentUser.fullName }}</p>
                <p>{{ currentUser.email }}</p>
              </div>
              <div>
                <button class="bg-linear-to-r from-blue-400 to-blue-600 py-2 px-6 rounded-lg cursor-pointer hover:opacity-80 text-white font-semibold">Edit</button>
              </div>
            </div>
          </div>

          <div v-if="route.path === '/profile'" class="w-full h-full flex justify-center items-center">
            <div class="w-full h-full grid grid-cols-2 gap-x-20 gap-y-10 mt-10">
              <div>
                <h1 class="font-semibold">ID</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.id }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Email</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.email }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Fullname</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.fullName }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Nickname</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.nickName }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Phonenumber</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.phoneNumber ? currentUser?.phoneNumber : "-" }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Bankaccount</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.bankAccount ? currentUser?.bankAccount : "-" }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Bankname</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.bankName ? currentUser?.bankName : "-" }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">User type</h1>
                <div class="flex justify-between items-center bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.userType}}</p>
                  <!-- <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"><path fill="#000000" fill-rule="evenodd" d="M4.43 8.512a.75.75 0 0 1 1.058-.081L12 14.012l6.512-5.581a.75.75 0 0 1 .976 1.138l-7 6a.75.75 0 0 1-.976 0l-7-6a.75.75 0 0 1-.081-1.057Z" clip-rule="evenodd"/></svg> -->
                </div>
              </div>
            </div>
          </div>

          <div v-if="route.path === '/profile/edit'" class="w-full h-full flex justify-center items-center">
            <form class="w-full h-full grid grid-cols-2 gap-x-20 gap-y-10 mt-10">
              <div>
                <h1 class="font-semibold">ID</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.id }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Email</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.email }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Fullname</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.fullName }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Nickname</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.nickName }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Phonenumber</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.phoneNumber ? currentUser?.phoneNumber : "-" }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Bankaccount</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.bankAccount ? currentUser?.bankAccount : "-" }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">Bankname</h1>
                <div class="bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.bankName ? currentUser?.bankName : "-" }}</p>
                </div>
              </div>

              <div>
                <h1 class="font-semibold">User type</h1>
                <div class="flex justify-between items-center bg-gray-200 p-3 rounded-lg">
                  <p>{{ currentUser?.userType}}</p>
                  <!-- <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"><path fill="#000000" fill-rule="evenodd" d="M4.43 8.512a.75.75 0 0 1 1.058-.081L12 14.012l6.512-5.581a.75.75 0 0 1 .976 1.138l-7 6a.75.75 0 0 1-.976 0l-7-6a.75.75 0 0 1-.081-1.057Z" clip-rule="evenodd"/></svg> -->
                </div>
              </div>
            </form>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
</style>