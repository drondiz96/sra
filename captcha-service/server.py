from flask import Flask, request, jsonify
import requests

app = Flask(__name__)

RECAPTCHA_SECRET = '6LfVUP4qAAAAANpieH7RbK_Yvzm5cKMum96X2FD4'  # Замените на ваш ключ

@app.route('/api/verify-recaptcha', methods=['POST'])
def verify_recaptcha():
    try:
        data = request.get_json()
        token = data.get('token', '')

        print('\n--- Новый запрос на проверку ---')
        print('Получен токен:', token[:10] + '...')

        google_response = requests.post(
            'https://www.google.com/recaptcha/api/siteverify',
            data={
                'secret': RECAPTCHA_SECRET,
                'response': token
            }
        )
        result = google_response.json()

        success = result.get('success', False)
        score = result.get('score', 0)
        action = result.get('action')
        hostname = result.get('hostname')
        error_codes = result.get('error-codes', [])

        print('Ответ от Google reCAPTCHA:', {
            'success': success,
            'score': score,
            'action': action,
            'hostname': hostname,
            'errorCodes': error_codes if error_codes else 'нет ошибок'
        })

        if success and score >= 0.5:
            print('✅ Проверка пройдена успешно')
            return jsonify({'success': True, 'score': score})
        else:
            error_msg = ', '.join(error_codes) if error_codes else 'Низкий рейтинг'
            print('❌ Проверка не пройдена:', error_msg)
            return jsonify({'success': False, 'error': error_msg, 'score': score}), 400

    except Exception as e:
        print('⚠️ Ошибка сервера:', str(e))
        return jsonify({'error': str(e)}), 500


if __name__ == '__main__':
    app.run(host="0.0.0.0",port=33001)
