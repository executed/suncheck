heroku ps:scale web=0 --app %1 && heroku ps:scale web=1 --app %1 && heroku logs -t --app %1 --tail

end:
