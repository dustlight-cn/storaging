/* tslint:disable */
/* eslint-disable */
/**
 * 对象存储服务
 * 提供文件、配置等对象存储服务。
 *
 * The version of the OpenAPI document: v1
 * Contact: hansin@goodvoice.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


import { Configuration } from './configuration';
import globalAxios, { AxiosPromise, AxiosInstance } from 'axios';
// Some imports not used depending on template conditions
// @ts-ignore
import { DUMMY_BASE_URL, assertParamExists, setApiKeyToObject, setBasicAuthToObject, setBearerAuthToObject, setOAuthToObject, setSearchParams, serializeDataIfNeeded, toPathString, createRequestFunction } from './common';
// @ts-ignore
import { BASE_PATH, COLLECTION_FORMATS, RequestArgs, BaseAPI, RequiredError } from './base';


/**
 * ConfigurationsApi - axios parameter creator
 * @export
 */
export const ConfigurationsApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * 获取应用的用户的配置。
         * @summary 获取用户配置
         * @param {string} name 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getConfiguration: async (name: string, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'name' is not null or undefined
            assertParamExists('getConfiguration', 'name', name)
            const localVarPath = `/v1/config/{name}`
                .replace(`{${"name"}}`, encodeURIComponent(String(name)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication auth required
            // oauth required
            await setOAuthToObject(localVarHeaderParameter, "auth", [], configuration)


    
            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 新增或更新用户配置。
         * @summary 设置用户配置
         * @param {string} name 
         * @param {{ [key: string]: object; }} requestBody 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        setConfiguration: async (name: string, requestBody: { [key: string]: object; }, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'name' is not null or undefined
            assertParamExists('setConfiguration', 'name', name)
            // verify required parameter 'requestBody' is not null or undefined
            assertParamExists('setConfiguration', 'requestBody', requestBody)
            const localVarPath = `/v1/config/{name}`
                .replace(`{${"name"}}`, encodeURIComponent(String(name)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'PUT', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication auth required
            // oauth required
            await setOAuthToObject(localVarHeaderParameter, "auth", [], configuration)


    
            localVarHeaderParameter['Content-Type'] = 'application/json';

            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(requestBody, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
    }
};

/**
 * ConfigurationsApi - functional programming interface
 * @export
 */
export const ConfigurationsApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = ConfigurationsApiAxiosParamCreator(configuration)
    return {
        /**
         * 获取应用的用户的配置。
         * @summary 获取用户配置
         * @param {string} name 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getConfiguration(name: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<{ [key: string]: object; }>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getConfiguration(name, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 新增或更新用户配置。
         * @summary 设置用户配置
         * @param {string} name 
         * @param {{ [key: string]: object; }} requestBody 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async setConfiguration(name: string, requestBody: { [key: string]: object; }, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.setConfiguration(name, requestBody, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
    }
};

/**
 * ConfigurationsApi - factory interface
 * @export
 */
export const ConfigurationsApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = ConfigurationsApiFp(configuration)
    return {
        /**
         * 获取应用的用户的配置。
         * @summary 获取用户配置
         * @param {string} name 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getConfiguration(name: string, options?: any): AxiosPromise<{ [key: string]: object; }> {
            return localVarFp.getConfiguration(name, options).then((request) => request(axios, basePath));
        },
        /**
         * 新增或更新用户配置。
         * @summary 设置用户配置
         * @param {string} name 
         * @param {{ [key: string]: object; }} requestBody 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        setConfiguration(name: string, requestBody: { [key: string]: object; }, options?: any): AxiosPromise<void> {
            return localVarFp.setConfiguration(name, requestBody, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * ConfigurationsApi - object-oriented interface
 * @export
 * @class ConfigurationsApi
 * @extends {BaseAPI}
 */
export class ConfigurationsApi extends BaseAPI {
    /**
     * 获取应用的用户的配置。
     * @summary 获取用户配置
     * @param {string} name 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ConfigurationsApi
     */
    public getConfiguration(name: string, options?: any) {
        return ConfigurationsApiFp(this.configuration).getConfiguration(name, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 新增或更新用户配置。
     * @summary 设置用户配置
     * @param {string} name 
     * @param {{ [key: string]: object; }} requestBody 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ConfigurationsApi
     */
    public setConfiguration(name: string, requestBody: { [key: string]: object; }, options?: any) {
        return ConfigurationsApiFp(this.configuration).setConfiguration(name, requestBody, options).then((request) => request(this.axios, this.basePath));
    }
}


/**
 * TestControllerApi - axios parameter creator
 * @export
 */
export const TestControllerApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        index: async (options: any = {}): Promise<RequestArgs> => {
            const localVarPath = `/index`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication auth required
            // oauth required
            await setOAuthToObject(localVarHeaderParameter, "auth", [], configuration)


    
            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @param {any} body 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        put: async (body: any, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'body' is not null or undefined
            assertParamExists('put', 'body', body)
            const localVarPath = `/put`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'PUT', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication auth required
            // oauth required
            await setOAuthToObject(localVarHeaderParameter, "auth", [], configuration)


    
            localVarHeaderParameter['Content-Type'] = 'image/_*';

            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(body, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
    }
};

/**
 * TestControllerApi - functional programming interface
 * @export
 */
export const TestControllerApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = TestControllerApiAxiosParamCreator(configuration)
    return {
        /**
         * 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async index(options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<object>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.index(options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @param {any} body 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async put(body: any, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.put(body, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
    }
};

/**
 * TestControllerApi - factory interface
 * @export
 */
export const TestControllerApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = TestControllerApiFp(configuration)
    return {
        /**
         * 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        index(options?: any): AxiosPromise<object> {
            return localVarFp.index(options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @param {any} body 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        put(body: any, options?: any): AxiosPromise<void> {
            return localVarFp.put(body, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * TestControllerApi - object-oriented interface
 * @export
 * @class TestControllerApi
 * @extends {BaseAPI}
 */
export class TestControllerApi extends BaseAPI {
    /**
     * 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof TestControllerApi
     */
    public index(options?: any) {
        return TestControllerApiFp(this.configuration).index(options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @param {any} body 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof TestControllerApi
     */
    public put(body: any, options?: any) {
        return TestControllerApiFp(this.configuration).put(body, options).then((request) => request(this.axios, this.basePath));
    }
}


