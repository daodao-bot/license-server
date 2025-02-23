<script setup lang="ts">
import { useWatermark } from "@pureadmin/utils";
import { nextTick, onBeforeUnmount, onMounted, ref } from "vue";

defineOptions({
  name: "Welcome"
});

const slogan = ref("license-admin");
const { setWatermark, clear } = useWatermark();

onMounted(() => {
  nextTick(() => {
    setWatermark(slogan.value, {
      globalAlpha: 0.15, // 值越低越透明
      gradient: [
        { value: 0, color: "magenta" },
        { value: 0.5, color: "blue" },
        { value: 1.0, color: "red" }
      ]
    });
  });
});

onBeforeUnmount(() => {
  // 在离开该页面时清除整页水印
  clear();
});
</script>

<template>
  <h1>License-Admin</h1>
</template>

<style lang="scss" scoped></style>
