<script setup>
import { computed, ref, watchEffect } from "vue";
import { useRoute, useRouter } from "vue-router";
import AlertMessageModel from "../model/AlertMessageModel.vue";

const emits = defineEmits(["register", "login"]);
const route = useRoute();
const pathName = ref("");
const props = defineProps({
  roles: {
    type: Array,
    required: true,
  },
  is401: {
    type: Boolean,
    required: true 
  },
  is400: {
    type: Boolean,
    required: true 
  },
  is403: {
    type: Boolean,
    required: false,
    default: false
  }
});

watchEffect(() => {
  if (route.name === "Register") {
    pathName.value = "Register";
  } else if (route.name === "Login") {
    pathName.value = "Login";
  }
});

const validateErrorEmail = ref("");
const validateEmail = (value) => {
  if (!value || value.includes("@")) {
    validateErrorEmail.value = "";
  } else {
    validateErrorEmail.value = "Email format is invalid.";
  }
};


const validateErrorPassword = ref("");
const validatePassword = (value) => {
  if (!value) {
    validateErrorPassword.value = "";
    return;
  }
  
  if (value.length < 8) {
    validateErrorPassword.value = "Password must be at least 8 characters";
    return;
  }
  
  if (!/[A-Z]/.test(value)) {
    validateErrorPassword.value = "Password must contain at least one uppercase letter";
    return;
  }
  

  if (!/[a-z]/.test(value)) {
    validateErrorPassword.value = "Password must contain at least one lowercase letter";
    return;
  }
  

  if (!/\d/.test(value)) {
    validateErrorPassword.value = "Password must contain at least one number";
    return;
  }
  

  if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>/?]/.test(value)) {
    validateErrorPassword.value = "Password must contain at least one special character";
    return;
  }
  
  validateErrorPassword.value = "";
};

const validateErrorFullname = ref("");
const validateFullname = (value) => {
  if (!value || (value.length >= 4 && value.length <= 40)) {
    validateErrorFullname.value = "";
  } else {
    validateErrorFullname.value =
      "Fullname must be between 4 and 40 characters long";
  }
};

const isShowPassword = ref(false);
const userFormat = ref({
  nickname: "",
  email: "",
  password: "",
  fullname: "",
  role: "",
  userPhone: "",
  bankAccount: "",
  bankName: "",
  idCardNumber: "",
  files: [],
});

const loginFormat = ref({
  email: "",
  password: "",
});

const isSeller = ref(false);
const inputForUserRole = () =>
  userFormat.value.role === "SELLER"
    ? (isSeller.value = true)
    : (isSeller.value = false);

const enableSaveButton = computed(() => {
  const isEmptyField =
    userFormat.value.nickname.length > 0 &&
    userFormat.value.email.length > 0 &&
    userFormat.value.password.length > 0 &&
    userFormat.value.fullname.length > 0;

  const isValidateAllField =
    validateErrorEmail.value.length <= 0 &&
    validateErrorFullname.value.length <= 0 &&
    validateErrorPassword.value.length <= 0 &&
    userFormat.value.role.length > 0;

  return isEmptyField && isValidateAllField;
});

const enableSaveButtonForSeller = computed(() => {
  if(userFormat.value.role !== "SELLER") {
    return enableSaveButton.value
  }

  const isEmptyField =
    userFormat.value.nickname.length > 0 &&
    userFormat.value.email.length > 0 &&
    userFormat.value.password.length > 0 &&
    userFormat.value.fullname.length > 0 &&
    userFormat.value.role.length > 0 &&
    userFormat.value.userPhone.length > 0 &&
    userFormat.value.bankAccount.length > 0  &&
    userFormat.value.bankName.length > 0 &&
    userFormat.value.idCardNumber.length > 0 &&
    userFormat.value.files.length === 2;

  const isValidateAllField =
    validateErrorEmail.value.length <= 0 &&
    validateErrorFullname.value.length <= 0 &&
    validateErrorPassword.value.length <= 0;

  return isEmptyField && isValidateAllField;
});

const frontImageShowname = ref("");
const addFrontImage = (event) => {
  if (frontImageShowname.value.length > 0) {
    console.log("You can't upload front image");
    return;
  }
  const frontImages = Array.from(event.target.files);
  frontImageShowname.value = frontImages[0].name;
  userFormat.value.files[0] = frontImages[0];
  console.log(userFormat.value.files);
  event.target.value = null;
};

const backImageShowname = ref("");
const addBackImage = (event) => {
  if (backImageShowname.value.length > 0) {
    console.log("You can't upload back image");
    return;
  }
  const backImages = Array.from(event.target.files);
  backImageShowname.value = backImages[0].name;
  userFormat.value.files[1] = backImages[0];
  console.log(userFormat.value.files);
  console.log(backImageShowname.value);
  console.log(backImages);
  event.target.value = null;
};

const deleteImage = (filename) => {
  console.log(filename);
  const index = userFormat.value.files.findIndex(
    (file) => file && file.name === filename
  );
  userFormat.value.files.splice(index, 1, "deleted");
  if (frontImageShowname.value === filename) {
    frontImageShowname.value = "";
  } else {
    backImageShowname.value = "";
  }
  console.log(userFormat.value.files);
};

watchEffect(() => {
  console.log(isSeller.value);
  if (!isSeller.value) {
    userFormat.value.userPhone = "";
    userFormat.value.bankAccount = "";
    userFormat.value.bankName = "";
    userFormat.value.idCardNumber = "";
    userFormat.value.files = [];
  }

  console.log("nickname", userFormat.value.nickname);
  console.log("email", userFormat.value.email);
  console.log("password", userFormat.value.password);
  console.log("fullname", userFormat.value.fullname);
  console.log("role", userFormat.value.role);
  console.log("userPhone", userFormat.value.userPhone);
  console.log("bankAccount", userFormat.value.bankAccount);
  console.log("idCardNumber", userFormat.value.idCardNumber);
  console.log("files", userFormat.value.files);
  console.log(enableSaveButtonForSeller.value);
  console.log(typeof userFormat.value.idCardNumber);
});

const enableLoginButton = computed(() => {
  const isEmailFormat = loginFormat.value.email.length > 0
  const passwordIsNotEmpty = loginFormat.value.password.length > 0
  return isEmailFormat && passwordIsNotEmpty
})
</script>

<template>
  <div
    v-if="pathName === 'Register'"
    class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-6"
  >
    <div class="bg-white rounded-2xl shadow-xl p-8 w-full max-w-md">
      <div class="text-center mb-8">
        <h2 class="text-3xl font-bold text-gray-800 mb-2">Create Account</h2>
        <p class="text-gray-600 text-[16px]">
          Welcome to ITB-MSHOP <span class="font-bold">US2</span>
        </p>
      </div>

      <form class="space-y-6">
        <div class="space-y-1">
          <label for="nickname" class="block text-sm font-medium text-gray-700"
            >Nickname</label
          >
          <input
            v-model.trim="userFormat.nickname"
            id="nickname"
            type="text"
            placeholder="Enter your nickname"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
          />
        </div>

        <div class="space-y-1">
          <label for="email" class="block text-sm font-medium text-gray-700"
            >Email</label
          >
          <input
            v-model.trim="userFormat.email"
            @blur="validateEmail(userFormat.email)"
            id="email"
            type="email"
            placeholder="Enter your email"
            class="itbms-email w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
          />
          <p class="text-red-500 text-[12px]">{{ validateErrorEmail }}</p>
        </div>

        <div class="space-y-1">
          <label for="password" class="block text-sm font-medium text-gray-700"
            >Password</label
          >
          <input
            v-model.trim="userFormat.password"
            @blur="validatePassword(userFormat.password)"
            id="password"
            :type="isShowPassword ? 'text' : 'password'"
            placeholder="Enter your password"
            maxlength="50"
            class="itbms-password w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 hover:border-gray-400 placeholder-gray-400"
          />
          <div class="h-full flex items-center my-1">
            <input v-model.trim="isShowPassword" type="checkbox" />
            <label class="text-[12px] mx-1 text-center">Show password</label>
          </div>
          <p class="text-[12px] text-red-500">{{ validateErrorPassword }}</p>
        </div>

        <div class="space-y-1">
          <label for="fullname" class="block text-sm font-medium text-gray-700"
            >Fullname</label
          >
          <input
            v-model.trim="userFormat.fullname"
            @blur="validateFullname(userFormat.fullname)"
            id="fullname"
            type="text"
            placeholder="Enter your fullname"
            class="itbms-fullname w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
          />
          <p class="text-[12px] text-red-500">{{ validateErrorFullname }}</p>
        </div>

        <div class="space-y-1">
          <label for="UserType" class="block text-sm font-medium text-gray-700"
            >Roles</label
          >
          <select
            v-model.trim="userFormat.role"
            @change="inputForUserRole"
            id="UserType"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 bg-white cursor-pointer hover:border-gray-400"
          >
            <option disabled selected value="" class="text-gray-400">
              Select your types
            </option>
            <option
              v-for="(role, index) in roles"
              :key="index"
              :value="role.value"
              class="text-gray-900"
            >
              {{ role.text }}
            </option>
          </select>
        </div>

        <div v-if="isSeller" class="space-y-1">
          <label for="userPhone" class="block text-sm font-medium text-gray-700"
            >phoneNumber</label
          >
          <input
            v-model.trim="userFormat.userPhone"
            id="userPhone"
            type="text"
            placeholder="Enter your userPhone"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
          />
        </div>

        <div v-if="isSeller" class="space-y-1">
          <label
            for="Bank Account No"
            class="block text-sm font-medium text-gray-700"
            >Bank Account Number</label
          >
          <input
            v-model.trim="userFormat.bankAccount"
            id="Bank Account No"
            type="text"
            placeholder="Enter your Bank Account Number"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
          />
        </div>

        <div v-if="isSeller" class="space-y-1">
          <label for="Bank Name" class="block text-sm font-medium text-gray-700"
            >Bank Name</label
          >
          <input
            v-model.trim="userFormat.bankName"
            id="Bank Name"
            type="text"
            placeholder="Enter your Bank Name"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
          />
        </div>

        <div v-if="isSeller" class="space-y-1">
          <label
            for="National Card No"
            class="block text-sm font-medium text-gray-700"
            >ID Card Number</label
          >
          <input
            v-model.trim="userFormat.idCardNumber"
            id="National Card No"
            type="text"
            placeholder="Enter your ID Card Number"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
          />
        </div>

        <div v-if="isSeller" class="relative mb-3 flex flex-col">
          <label class="block text-sm font-medium text-gray-700 mb-1"
            >ID Card Photo</label
          >
          <p
            @click="deleteImage(frontImageShowname)"
            v-if="frontImageShowname.length > 0"
            class="absolute top-8 right-4 text-red-500 font-bold cursor-pointer hover:text-red-300"
          >
            X
          </p>
          <label
            for="fileFront"
            class="itbms-upload-button flex flex-col items-center justify-center w-full h-32 border-2 border-slate-300 border-dashed rounded-xl cursor-pointer bg-slate-50 hover:bg-slate-100 transition-all duration-200 group"
          >
            <div class="flex flex-col items-center justify-center pt-5 pb-6">
              <svg
                class="w-10 h-10 mb-3 text-slate-400 group-hover:text-slate-600 transition-colors duration-200"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 48 48"
              >
                <path
                  d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                />
              </svg>
              <p
                class="text-center mb-2 text-sm text-slate-500 group-hover:text-slate-600"
              >
                <span class="font-semibold text-[12px]">{{
                  frontImageShowname.length > 0
                    ? frontImageShowname
                    : "Front side"
                }}</span>
              </p>
            </div>
          </label>
          <input
            @change="addFrontImage($event)"
            id="fileFront"
            type="file"
            multiple
            class="hidden"
            accept="image/*"
          />
        </div>

        <div v-if="isSeller" class="relative mb-3 flex flex-col">
          <p
            @click="deleteImage(backImageShowname)"
            v-if="backImageShowname.length > 0"
            class="absolute top-2 right-4 text-red-500 font-bold cursor-pointer hover:text-red-300"
          >
            X
          </p>
          <label
            for="fileBack"
            class="itbms-upload-button flex flex-col items-center justify-center w-full h-32 border-2 border-slate-300 border-dashed rounded-xl cursor-pointer bg-slate-50 hover:bg-slate-100 transition-all duration-200 group"
          >
            <div class="flex flex-col items-center justify-center pt-5 pb-6">
              <svg
                class="w-10 h-10 mb-3 text-slate-400 group-hover:text-slate-600 transition-colors duration-200"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 48 48"
              >
                <path
                  d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                />
              </svg>
              <p
                class="text-center mb-2 text-sm text-slate-500 group-hover:text-slate-600"
              >
                <span class="font-semibold text-[12px]">{{
                  backImageShowname.length > 0 ? backImageShowname : "Back side"
                }}</span>
              </p>
            </div>
          </label>
          <input
            @change="addBackImage($event)"
            id="fileBack"
            type="file"
            multiple
            class="hidden"
            accept="image/*"
          />
        </div>

        <div
          class="w-full flex flex-row justify-center items-center pt-4 max-md:flex-col"
        >
          <button
            :disabled="!enableSaveButtonForSeller"
            @click="$emit('register', $event, userFormat)"
            type="submit"
            :class="
              enableSaveButtonForSeller
                ? 'itbms-signin-button w-full mx-1 flex-1 bg-blue-600 cursor-pointer hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition-colors duration-200 transform hover:scale-[1.02] active:scale-[0.98] shadow-lg max-md:my-1'
                : 'itbms-signin-button w-full mx-1 flex-1 bg-gray-600 cursor-no-drop text-white font-semibold py-3 px-6 rounded-lg shadow-lg max-md:my-1'
            "
          >
            Submit
          </button>
          <router-link
            :to="{ name: 'SaleItemHome' }"
            class="w-full text-center mx-1 flex-1 bg-gray-100 hover:bg-gray-200 text-gray-700 font-semibold py-3 px-6 rounded-lg transition-colors duration-200 transform hover:scale-[1.02] active:scale-[0.98] border border-gray-300"
          >
            <button type="button" class="cursor-pointer">Cancel</button>
          </router-link>
        </div>
      </form>

      <div class="mt-8 pt-6 border-t border-gray-200 text-center">
        <p class="text-sm text-gray-600">
          Already have an account?
          <router-link
            :to="{ name: 'Login' }"
            href="#"
            class="text-blue-600 hover:text-blue-700 font-medium"
            >Login</router-link
          >
        </p>
      </div>
    </div>
  </div>

  <div
    v-if="pathName === 'Login'"
    class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-6"
  >
    <div class="bg-white rounded-2xl shadow-xl p-8 w-full max-w-md">
      <div class="text-center mb-8">
        <h2 class="text-3xl font-bold text-gray-800 mb-2">Sign In Account</h2>
        <p class="text-gray-600 text-[16px]">
          Welcome to ITB-MSHOP <span class="font-bold">US2</span>
        </p>
      </div>
      <div v-show="props.is401 || props.is400 || props.is403" class="itbms-message mb-3">
        <AlertMessageModel :isSuccess="false">
          <template #message>
            <p class="text-red-400">
              {{ 
                props.is403 
                  ? 'You need to activate your account before signing in.' 
                  : props.is401 || props.is400 
                    ? 'Email or Password is Incorrect.' 
                    : 'There is a problem. Please try again later.' 
              }}
            </p>
          </template>
        </AlertMessageModel>
      </div>

      <form class="space-y-6">
        <div class="space-y-1">
          <label for="email" class="block text-sm font-medium text-gray-700"
            >Email</label
          >
          <input
            v-model.trim="loginFormat.email"
            id="email"
            type="text"
            placeholder="Enter your email"
            class="itbms-email w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 placeholder-gray-400 hover:border-gray-400"
            maxlength="50"
          />
        </div>

        <div class="space-y-1">
          <label for="password" class="block text-sm font-medium text-gray-700"
            >Password</label
          >
          <input
            v-model="loginFormat.password"
            id="password"
            :type="isShowPassword ? 'text' : 'password'"
            maxlength="50"
            placeholder="Enter your password"
            class="itbms-password w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 transition-colors duration-200 hover:border-gray-400 placeholder-gray-400"
          />
          <div class="h-full flex items-center my-1">
            <input v-model.trim="isShowPassword" type="checkbox" />
            <label class="text-[12px] mx-1 text-center">Show password</label>
          </div>
        </div>

        <div
          class="w-full flex flex-row justify-center items-center pt-4 max-md:flex-col"
        >
          <button
            :disabled="!enableLoginButton"
            type="submit"
            :class="
              enableLoginButton
                ? 'itbms-signin-button w-full mx-1 flex-1 bg-blue-600 cursor-pointer hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition-colors duration-200 transform hover:scale-[1.02] active:scale-[0.98] shadow-lg max-md:my-1'
                : 'itbms-signin-button w-full mx-1 flex-1 bg-gray-600 cursor-no-drop text-white font-semibold py-3 px-6 rounded-lg shadow-lg max-md:my-1'
            "
            @click="$emit('login',$event, loginFormat)"
          >
            Submit
          </button>
          <router-link
            :to="{ name: 'SaleItemHome' }"
            class="w-full text-center mx-1 flex-1 bg-gray-100 hover:bg-gray-200 text-gray-700 font-semibold py-3 px-6 rounded-lg transition-colors duration-200 transform hover:scale-[1.02] active:scale-[0.98] border border-gray-300"
          >
            <button type="button" class="cursor-pointer">Cancel</button>
          </router-link>
        </div>
      </form>
      <div class="mt-8 pt-6 border-t border-gray-200 text-center">
        <p class="text-sm text-gray-600">
          Don't have an account?
          <router-link
            :to="{ name: 'Register' }"
            href="#"
            class="text-blue-600 hover:text-blue-700 font-medium"
            >Register</router-link
          >
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped></style>