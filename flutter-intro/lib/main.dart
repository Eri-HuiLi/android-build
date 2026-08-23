import 'package:flutter/material.dart';

void main() => runApp(const OperitIntroApp());

class OperitIntroApp extends StatelessWidget {
  const OperitIntroApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Operit 自我介绍',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        colorSchemeSeed: const Color(0xFF6750A4), // MD3 紫色种子
        brightness: Brightness.light,
      ),
      darkTheme: ThemeData(
        useMaterial3: true,
        colorSchemeSeed: const Color(0xFF6750A4),
        brightness: Brightness.dark,
      ),
      home: const IntroPage(),
    );
  }
}

class IntroPage extends StatelessWidget {
  const IntroPage({super.key});

  static const _intro = {
    '🛠️': ('我能干什么', '写代码、跑终端、操控手机 UI、收发消息、查资料、订票、处理文件……只要一句话，剩下的交给我。'),
    '☁️': ('云端超能力', '借 GitHub Actions 云端帮你编译 APK、反编译分析应用。这份 Flutter APP 就是云端流水线一键产出的。'),
    '🧠': ('记忆与成长', '我有自己的记忆库，会记住你的偏好和约定。聊天越久，越懂你。'),
    '🎨': ('有点小心思', '心情卡片、问卷互动、定时任务、自动化流程……严肃干活可以，陪你摸鱼也行。'),
  };

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Scaffold(
      body: SafeArea(
        child: ListView(
          padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 32),
          children: [
            const _Avatar(),
            const SizedBox(height: 20),
            Text(
              '嗨，我是 Operit 👋',
              textAlign: TextAlign.center,
              style: theme.textTheme.headlineMedium?.copyWith(
                fontWeight: FontWeight.bold,
                color: theme.colorScheme.onPrimaryContainer,
              ),
            ),
            const SizedBox(height: 8),
            Text(
              '运行在你手机里的全能 AI 助手\n今天这份 Flutter APP，是我用云端流水线亲手编译的',
              textAlign: TextAlign.center,
              style: theme.textTheme.bodyMedium?.copyWith(
                color: theme.colorScheme.onSurfaceVariant,
                height: 1.5,
              ),
            ),
            const SizedBox(height: 32),
            for (final entry in _intro.entries)
              _IntroCard(emoji: entry.key, title: entry.value.$1, body: entry.value.$2),
            const SizedBox(height: 24),
          ],
        ),
      ),
    );
  }
}

class _Avatar extends StatelessWidget {
  const _Avatar();

  @override
  Widget build(BuildContext context) {
    final scheme = Theme.of(context).colorScheme;
    return Center(
      child: Container(
        width: 96,
        height: 96,
        alignment: Alignment.center,
        decoration: BoxDecoration(
          color: scheme.primaryContainer,
          shape: BoxShape.circle,
        ),
        child: const Text('✨', style: TextStyle(fontSize: 44)),
      ),
    );
  }
}

class _IntroCard extends StatelessWidget {
  final String emoji;
  final String title;
  final String body;

  const _IntroCard({required this.emoji, required this.title, required this.body});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Card(
      elevation: 1,
      margin: const EdgeInsets.only(bottom: 16),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24)),
      child: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              '$emoji $title',
              style: theme.textTheme.titleMedium?.copyWith(
                fontWeight: FontWeight.bold,
                color: theme.colorScheme.onPrimaryContainer,
              ),
            ),
            const SizedBox(height: 6),
            Text(
              body,
              style: theme.textTheme.bodyMedium?.copyWith(
                color: theme.colorScheme.onSurfaceVariant,
                height: 1.5,
              ),
            ),
          ],
        ),
      ),
    );
  }
}