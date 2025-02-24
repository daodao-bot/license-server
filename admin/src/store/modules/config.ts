import { defineStore } from "pinia";
import { store } from "@/store";
import { adminConfig, type ConfigData } from "@/api/admin";

const CONFIG = "LICENSE-ADMIN-CONFIG";

export const useConfigStore = defineStore("license-config", {
  state: () => ({
    config: null as unknown as ConfigData
  }),
  getters: {},
  actions: {
    async getConfig(): Promise<ConfigData> {
      const configValue = sessionStorage.getItem(CONFIG);
      if (configValue) {
        this.config = JSON.parse(configValue);
        return this.config;
      } else {
        await adminConfig({ param: {} })
          .then(({ data }) => {
            this.setConfig(data);
            return this.config;
          })
          .catch(e => {
            console.error(e);
          });
      }
    },
    async setConfig(config: ConfigData): Promise<void> {
      this.config = config;
      sessionStorage.setItem(CONFIG, JSON.stringify(config));
    },
    async removeConfig(): Promise<void> {
      sessionStorage.removeItem(CONFIG);
      this.config = null as unknown as ConfigData;
    }
  }
});

export function useConfigStoreHook() {
  return useConfigStore(store);
}
