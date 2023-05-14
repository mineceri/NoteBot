import jaydebeapi

# Замените настроечные параметры базы данных на свои
jdbc_url = "jdbc:h2:file:../local_database/NoteDB_Test;IFEXISTS=TRUE;AUTO_SERVER=TRUE;"
jdbc_driver = "org.h2.Driver"
jdbc_jar = "h2-driver.jar"

def clear_database():
    conn = jaydebeapi.connect(jdbc_driver, jdbc_url, {"user": "sa", "password": ""}, jdbc_jar)

    try:
        cursor = conn.cursor()

        # Получение списка таблиц
        cursor.execute("SHOW TABLES")
        tables = cursor.fetchall()

        # Очистка таблиц
        for table in tables:
            table_name = table[0]
            cursor.execute(f"DELETE FROM {table_name}")

        conn.commit()
    finally:
        cursor.close()
        conn.close()

if __name__ == '__main__':
    clear_database()
    print("Database cleared successfully!")
