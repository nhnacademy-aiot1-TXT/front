async function illuminationCountCall() {
    const url = 'http://localhost:8000/api/sensor/illumination'; // Ensure the endpoint is correct and accessible

    const option = {
        method : "GET",
        headers : {
            Authorization: accessToken
        }
    }
    const response = await fetch(url, option);
    return await response.json();
}

async function updateIllumination() {
    const data = await illuminationCountCall();
    const value = data.value;
    console.log(data);
    const gaugeElement = document.getElementById('illumination');
    $(gaugeElement).data("used", value).data("text", value);
    $(gaugeElement).gaugeMeter();
}

