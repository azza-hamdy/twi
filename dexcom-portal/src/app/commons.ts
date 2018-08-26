export function showLoading() {
	console.log("loading ...");    
}

export function hideLoading() {
	console.log("loaded");
}

export function doNothing () {}

export const webServiceEndpoint: string = 'http://localhost:8086/dexcom'; 
//  export const webServiceEndpoint: string = 'https://twi-services.com:8086/dexcom'; 