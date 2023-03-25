# House Hunter Application(FUNDA.NL)

## Configuration

SMTP Mail Server Required to run the services. Check the link how to use GMAIL as SMTP Server:

| Config Name          | Description               |
|----------------------|---------------------------|
| SMTP_SERVER_HOST     | Host Name for SMTP Server |
| SMTP_SERVER_PORT     | Port for SMTP Server      |
| SMTP_SERVER_USERNAME | Host Name for SMTP Server |
| SMTP_SERVER_PASSWORD | Host Name for SMTP Server |


| Config Name            | Description                                 |
|------------------------|---------------------------------------------|
| MAIL_NOTIFY_RECIPIENTS | Recipients' addressesto notify new adverts  |
| MAIL_NOTIFY_SENDER     | Sender mail address                         |

The listes parameter should set as environment variable in the file, `docker-compose.yml`.

## Running Services

```bash
docker-compose up
```

## Adding filters

Check the example filters in the file, `deployment/db/sample_filters.sql`.

Example filter for: https://www.funda.nl/koop/eindhoven/waterrijk/0-600000/sorteer-datum-af/
```bash
docker exec -it db psql -U house_hunter_app house_hunter -c "insert into public.house_filter_entity (id, base_url, district, province, max, min, sort_order) values (3, 'https://www.funda.nl/koop/', 'waterrijk', 'Eindhoven', 600000, 0, 'DESC_DATE');"
```




