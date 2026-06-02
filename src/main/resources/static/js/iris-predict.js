let chart = null;

function fillAndPredict(sl, sw, pl, pw){
    document.getElementById('sepalLength').value = sl;
    document.getElementById('sepalWidth').value = sw;
    document.getElementById('petalLength').value = pl;
    document.getElementById('petalWidth').value = pw;
    predict();
}
async function predict(){
    const data = {
        sepalLength: document.getElementById('sepalLength').value,
        sepalWidth: document.getElementById('sepalWidth').value,
        petalLength: document.getElementById('petalLength').value,
        petalWidth: document.getElementById('petalWidth').value
    };

    try{
        const response = await fetch('/api/iris/predict',{
            method: 'POST',
            headers: { 'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });

        const result = await response.json();

        document.getElementById('result-area').style.display = 'block';
        document.getElementById('predictedSpecies').innerText = result.predictedSpecies;
        renderChart(result.probabilities);

    }catch (error){
        console.error('Error:', error)
        alert('예측 중 오류가 발생했습니다.');
    }
}

function renderChart(probabilities){
    const ctx = document.getElementById('probabilityChart').getContext('2d');
    const labels = Object.keys(probabilities);
    const values = Object.values(probabilities);
    if (chart){
        chart.destroy();
    }

        chart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: '품종별 확률',
                    data: values,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.5)',
                        'rgba(54, 162, 235, 0.5)',
                        'rgba(75, 192, 192, 0.5)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(75, 192, 192, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: { beginAtZero: true, max: 1 }
                }
            }
        });
}
