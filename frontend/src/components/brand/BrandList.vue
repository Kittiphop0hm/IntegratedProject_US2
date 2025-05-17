<script setup>
import ListTableModel from "../model/ListTableModel.vue";
import { ref } from "vue";
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
        <td class="border px-2 py-1 itbms-brand">
          {{ yourItem.name ?? "-" }}
        </td>
      </template>
      <template #action="{ yourItem }">
        <div class="flex justify-center items-center">
          <router-link :to="{ name: 'BrandEdit', params: { id: yourItem.id } }">
            <button class="itbms-edit-button btn btn-outline btn-info mr-2 my-2">
              <svg class="fill-current" xmlns="http://www.w3.org/2000/svg" width="25" height="32" viewBox="0 0 24 24"><!-- Icon from Material Symbols Light by Google - https://github.com/google/material-design-icons/blob/master/LICENSE --><path d="M5.616 20q-.691 0-1.153-.462T4 18.384V5.616q0-.691.463-1.153T5.616 4h8.386l-1 1H5.616q-.231 0-.424.192T5 5.616v12.769q0 .23.192.423t.423.192h12.77q.23 0 .423-.192t.192-.423v-7.489l1-1v8.489q0 .69-.462 1.153T18.384 20zM10 14v-2.615l8.944-8.944q.166-.166.348-.23t.385-.063q.189 0 .368.064t.326.21L21.483 3.5q.16.166.242.365t.083.4t-.061.382q-.06.18-.226.345L12.52 14zm10.814-9.715l-1.112-1.17zM11 13h1.092l6.666-6.666l-.546-.546l-.61-.584L11 11.806zm7.212-7.211l-.61-.585zl.546.546z"/></svg>
            </button>
          </router-link>

          <!--  -->
          <router-link to=/>
            <button class="itbms-delete-button btn btn-outline btn-error ml-2 my-2">
              <svg class="fill-current" xmlns="http://www.w3.org/2000/svg" width="25" height="32" viewBox="0 0 24 24"><!-- Icon from Material Symbols Light by Google - https://github.com/google/material-design-icons/blob/master/LICENSE --><path d="M7 6v13zm3.866 14h-3.25q-.691 0-1.153-.462T6 18.384V6H5V5h4v-.77h6V5h4v1h-1v4.242q-.213-.048-.494-.068t-.506-.02V6H7v12.385q0 .23.192.423t.423.192h2.839q.07.271.187.536q.117.264.224.464m-1.211-3h.5q0-1.056.25-1.78l.25-.724V8h-1zm3.692-5.788q.213-.14.481-.281q.27-.14.52-.227V8h-1zm3.65 10.634q-2.013 0-3.428-1.418q-1.414-1.42-1.414-3.432t1.418-3.428q1.42-1.414 3.432-1.414t3.428 1.419q1.414 1.419 1.414 3.431t-1.418 3.428t-3.432 1.414m1.654-2.65l.546-.546l-1.811-1.804v-2.73h-.77v3.046z"/></svg>
            </button>
          </router-link>
        </div>
      </template>
    </ListTableModel>
  </div>
</template>
