<script setup>
import ListTableModel from "../model/ListTableModel.vue";
import DeletePopupModel from "../model/DeletePopupModel.vue";
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { getItems } from "@/libs/fetchUtil";
defineEmits(['deleteProduct'])
const props = defineProps({
  brands: {
    type: Array,
    required: true,
  },
});
</script>

<template>
  <div class="px-10">
    <ListTableModel :items="brands">
      <template #listItem="{ yourItem }">
        <td class="border px-2 py-1 itbms-id">{{ yourItem.id ?? "-" }}</td>
        <td class="border px-2 py-1 itbms-name">
          {{ yourItem.name ?? "-" }}
        </td>
      </template>

      <template #action="{ yourItem }">
        <div class="w-full py-2 flex justify-center">
          <router-link :to="{ name: 'BrandEdit', params: { id: yourItem.id } }">
            <button class="itbms-edit-button btn btn-info mr-2">
              <svg
                class="fill-current"
                xmlns="http://www.w3.org/2000/svg"
                width="25"
                height="32"
                viewBox="0 0 24 24"
              >
                <!-- Icon from Material Symbols by Google - https://github.com/google/material-design-icons/blob/master/LICENSE -->
                <path
                  d="M9 9V7h9v2zm0 3v-2h9v2zm3 8H5zm0 2H6q-1.25 0-2.125-.875T3 19v-3h3V2h15v9.025q-.5-.05-1.012.038t-.988.312V4H8v12h6l-2 2H5v1q0 .425.288.713T6 20h6zm2 0v-3.075l5.525-5.5q.225-.225.5-.325t.55-.1q.3 0 .575.113t.5.337l.925.925q.2.225.313.5t.112.55t-.1.563t-.325.512l-5.5 5.5zm7.5-6.575l-.925-.925zm-6 5.075h.95l3.025-3.05l-.45-.475l-.475-.45l-3.05 3.025zm3.525-3.525l-.475-.45l.925.925z"
                />
              </svg>
            </button>
          </router-link>
          <button
            class="btn btn-error ml-2 itbms-delete-button"
            @click="$emit('deleteProduct',yourItem)"
          >
          <!-- @click="showDeleteConfirm(yourItem)" -->
            <svg
              class="fill-current"
              xmlns="http://www.w3.org/2000/svg"
              width="25"
              height="32"
              viewBox="0 0 24 24"
            >
              <!-- Icon from Material Symbols by Google - https://github.com/google/material-design-icons/blob/master/LICENSE -->
              <path
                d="M7 6v13zm4.25 15H7q-.825 0-1.412-.587T5 19V6H4V4h5V3h6v1h5v2h-1v4.3q-.425-.125-.987-.213T17 10V6H7v13h3.3q.15.525.4 1.038t.55.962M9 17h1q0-1.575.5-2.588L11 13.4V8H9zm4-5.75q.425-.275.963-.55T15 10.3V8h-2zM17 22q-2.075 0-3.537-1.463T12 17t1.463-3.537T17 12t3.538 1.463T22 17t-1.463 3.538T17 22m1.65-2.65l.7-.7l-1.85-1.85V14h-1v3.2z"
              />
            </svg>
          </button>
        </div>
      </template>
    </ListTableModel>
    
    <!-- <DeletePopupModel
      v-if="confirmDelete"
      @cancel-delete="cancelDelete"
      @delete-sale-item="deleteBrand"
    >
      <template #message>
        <span class="itbms-message font-semibold">
          Do you want to delete {{ selectedBrandName }} brand?
        </span>
      </template>
    </DeletePopupModel> -->




  </div>
</template>
