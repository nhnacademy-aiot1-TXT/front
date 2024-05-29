document.addEventListener("DOMContentLoaded", function() {
    const alarmListContainer = document.getElementById('alarmList');

    function isEmptyValue(value) {
        return value === null || value === undefined || value.trim() === '';
    }

    async function fetchAndDisplayNotifications() {


        // console.log("accessTokent is " + accessToken)
        // console.log("accessTokentTemp is " + accessTokenTemp)

        let accessTokenfinal = accessToken;
        if (isEmptyValue(accessToken)) {
            accessTokenfinal = accessTokenTemp;
            // console.log("After processed accessTokent is " + accessTokenfinal)
        }

        try {

            const option = {
                method : "GET",
                headers : {
                    Authorization: accessTokenfinal
                }

            }
            const response = await fetch('http://localhost:8000/api/common/notification', option);

            // console.log(response)

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const notifications = await response.json();

            // 시간 기준으로 정렬
            notifications.sort((a, b) => new Date(b.time) - new Date(a.time));

            const chunk = notifications.slice(0,5);

            const alarmListHtml = chunk.map(notification => `
                <li class="mb-2">
                    <a class="dropdown-item border-radius-md">
                        <div class="d-flex py-1">
                            <div class="my-auto">
                                <img src="/img/illustrations/bell_icon.svg" class="avatar avatar-sm me-3">
                            </div>
                            <div class="d-flex flex-column justify-content-center">
                                <h6 class="text-sm font-weight-normal mb-1">
                                    <span class="font-weight-bold">${notification.contents}</span>
                                </h6>
                                <p class="text-xs text-secondary mb-0">
                                    <i class="fa fa-clock me-1"></i>
                                    ${new Date(notification.time).toLocaleString()}
                                </p>
                            </div>
                        </div>
                    </a>
                </li>
            `).join('');

            alarmListContainer.innerHTML = alarmListHtml;
        } catch (error) {
            console.error('Failed to fetch notifications:', error);
        }
    }

    fetchAndDisplayNotifications();
});
