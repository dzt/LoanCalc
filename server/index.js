const request = require('request');
const express = require('express');
const cheerio = require('cheerio');
const moment = require('moment');
const bodyParser = require('body-parser');
const http = require('http');
const app = express();
app.server = http.createServer(app);

app.use(bodyParser.json({
    limit: '50mb'
}));

app.use(bodyParser.urlencoded({
    extended: true,
    limit: '50mb'
}));

app.get('/', (req, res) => {
    res.json({
        message: 'Car Loan API v1 by Jack Hill'
    })
});

app.post('/fetch', (req, res) => {

    let purchase_price = req.body.purchase_price;
    let down_payment = req.body.down_payment;
    let interest_rate = req.body.interest_rate;
    let sales_tax_rate = req.body.sales_tax_rate;
    let term = req.body.term;

    fetchCSRFToken((error, token) => {

        if (error) return res.status(400).send({
            error: true,
            message: "Error Occured while trying to fetch token."
        });

        let form = {
            'utf8': '✓',
            'purchase_price': purchase_price,
            'down_payment': down_payment,
            'trade_in': '0.00',
            'owed_on_trade': '0.00',
            'interest_rate': interest_rate,
            'sales_tax_rate': sales_tax_rate,
            'term': term,
            'start_date': moment().format('YYYY-MM-DD'),
            'sales_tax_type': 'after_trade',
            'commit': 'Calculate'
        };

        request({
            url: `https://www.calculatestuff.com/financial/auto-loan-calculator`,
            followAllRedirects: true,
            method: 'POST',
            headers: {
                'Accept': '*/*;q=0.5, text/javascript, application/javascript, application/ecmascript, application/x-ecmascript',
                'Accept-Language': 'en-US,en;q=0.9',
                'Connection': 'keep-alive',
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                'Host': 'www.calculatestuff.com',
                'Origin':' https://www.calculatestuff.com',
                'Referer': 'https://www.calculatestuff.com/financial/auto-loan-calculator',
                'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36',
                'X-CSRF-Token': token,
                'X-Requested-With': 'XMLHttpRequest'
            },
            formData: form
        }, (err, response, body) => {
    
            if (err || !body) {
                return res.status(400).send({
                    error: true,
                    message: "Error Occured while trying to load data, please try again."
                });
            }

            let $ = cheerio.load(body);
    
            let data = {
                monthlyPayment: $('.big.bold').eq(0).text(),
                loanAmount: $('.big.bold').eq(1).text(),
                totalInterest: $('.big.bold').eq(2).text(),
                payOffDate: $('.big.bold').eq(4).text(),
                schedule: []
            }

    
            let modifiedBody = body.split('var amortizationData = ')[1].split(';')[0];
            data.schedule = JSON.parse(modifiedBody);
    
            return res.status(200).send({
                error: false,
                data: data
            });
            
        });
    });

});

let fetchCSRFToken = (cb) => {
    cb = cb || function(){};
    request({
        url: `https://www.calculatestuff.com/financial/auto-loan-calculator`,
        followAllRedirects: true,
        method: 'get',
        headers: {
            'Accept-Language': 'en-US,en;q=0.9',
            'Connection': 'keep-alive',
            'Host': 'www.calculatestuff.com',
            'Origin':' https://www.calculatestuff.com',
            'Referer': 'https://www.calculatestuff.com/financial/auto-loan-calculator',
            'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36',
        }
    }, (err, response, body) => {
        if (err || !body) {
            return cb(true, null);
        } else {
            let $ = cheerio.load(body);
            let token = $('meta[name="csrf-token"]').attr('content');
            return cb(null, token);
        }
    });
}

let formatDate = (date) => {
    let length = countCharacter('/');
    let newDate = date;
    for (let i = 0; i < length; i++) {
        newDate.replace('/', '-');
    }
    return newDate; 
}

let countCharacter = (str, char) => {
    return str.length - str.replace(new RegExp(char,"g"),"").length;
}

app.server.listen(process.env.PORT || 3000, () => {
    console.log(`Server is running at http://127.0.0.1:3000`);
});