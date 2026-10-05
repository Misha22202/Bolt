<script setup>

</script>

<template>
<section class="map-section">
  <span class="tag">Как нас найти</span>
  <div class="map-wrapper">
    <div style="position:relative;overflow:hidden;"><a href="https://yandex.ru/maps/org/pervy_vint/35018072179/?utm_medium=mapframe&utm_source=maps" style="color:#eee;font-size:12px;position:absolute;top:0px;">Первый Винт</a><a href="https://yandex.ru/maps/193/voronezh/category/electrical_equipment_repairs/184107046/?utm_medium=mapframe&utm_source=maps" style="color:#eee;font-size:12px;position:absolute;top:14px;">Ремонт электрооборудования в Воронеже</a><a href="https://yandex.ru/maps/193/voronezh/category/appliance_repair/184108223/?utm_medium=mapframe&utm_source=maps" style="color:#eee;font-size:12px;position:absolute;top:28px;">Ремонт бытовой техники в Воронеже</a><iframe src="https://yandex.ru/map-widget/v1/?ll=39.147502%2C51.721826&mode=search&oid=35018072179&ol=biz&z=17.05" width="560" height="400" allowfullscreen="true" style="position:relative;"></iframe></div>
  </div>
</section>
</template>

<style scoped>
.map-section {
  padding: 48px 0 0;
}
.tag {
  display: block;
  margin-bottom: 16px;
  font-size: 12px;
  font-weight: 750;
  letter-spacing: 1.4px;
  text-transform: uppercase;
  color: var(--accent-dark);
}
.map-wrapper {
  border-radius: var(--radius);
  overflow: hidden;
  border: 2px solid var(--line);
}
.map-wrapper iframe {
  display: block;
  width: 100%;
  border: 1px solid transparent;
}

@media screen and (max-width: 640px) {
  .map-wrapper iframe {
    height: 300px;
  }
}
</style>